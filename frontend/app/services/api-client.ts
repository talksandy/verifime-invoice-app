/**
 * API Client Abstraction
 * Handles:
 * - Base URL configuration
 * - Request/response interceptors
 * - Timeout handling with AbortController
 * - Automatic retry with exponential backoff
 * - Centralized error handling
 */

import { API } from "../constants";

interface RequestConfig {
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
  headers?: Record<string, string>;
  body?: unknown;
  timeout?: number;
  retryAttempts?: number;
}

const DEFAULT_TIMEOUT = API.TIMEOUT;
const DEFAULT_RETRY_ATTEMPTS = API.RETRY_ATTEMPTS;
const RETRY_DELAY = 1000; // 1 second initial delay

class ApiClient {
  private baseUrl: string;

  constructor(baseUrl: string) {
    if (!baseUrl) {
      throw new Error(
        'API_URL is not configured. Please set NEXT_PUBLIC_API_URL environment variable.'
      );
    }
    this.baseUrl = baseUrl;
  }

  /**
   * Execute request with retry logic and timeout
   */
  async request<T>(
    endpoint: string,
    config: RequestConfig = {}
  ): Promise<T> {
    const {
      method = 'GET',
      headers = {},
      body,
      timeout = DEFAULT_TIMEOUT,
      retryAttempts = DEFAULT_RETRY_ATTEMPTS,
    } = config;

    let lastError: Error | null = null;

    // Retry loop with exponential backoff
    for (let attempt = 0; attempt < retryAttempts; attempt++) {
      try {
        const response = await this.fetchWithTimeout(
          `${this.baseUrl}${endpoint}`,
          {
            method,
            headers: {
              'Content-Type': 'application/json',
              ...headers,
            },
            body: body ? JSON.stringify(body) : undefined,
          },
          timeout
        );

        // Handle non-OK responses
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(
            `HTTP ${response.status}: ${errorText || response.statusText}`
          );
        }

        // Parse and return response
        const contentType = response.headers.get('content-type');
        if (contentType?.includes('application/json')) {
          return await response.json();
        } else {
          return (await response.text()) as T;
        }
      } catch (error) {
        lastError = error instanceof Error ? error : new Error(String(error));
        
        // Don't retry on the last attempt
        if (attempt < retryAttempts - 1) {
          const delay = RETRY_DELAY * Math.pow(2, attempt);
          console.warn(
            `Request failed (attempt ${attempt + 1}/${retryAttempts}): ${lastError.message}. Retrying in ${delay}ms...`
          );
          await this.sleep(delay);
        }
      }
    }

    // All retries exhausted
    throw new Error(
      `Request failed after ${retryAttempts} attempts: ${lastError?.message}`
    );
  }

  /**
   * Fetch with timeout using AbortController
   */
  private fetchWithTimeout(
    url: string,
    options: RequestInit,
    timeout: number
  ): Promise<Response> {
    return new Promise((resolve, reject) => {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), timeout);

      fetch(url, { ...options, signal: controller.signal })
        .then(resolve)
        .catch((error) => {
          if (error.name === 'AbortError') {
            reject(new Error(`Request timeout after ${timeout}ms`));
          } else {
            reject(error);
          }
        })
        .finally(() => clearTimeout(timeoutId));
    });
  }

  /**
   * Sleep utility for retry delays
   */
  private sleep(ms: number): Promise<void> {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

  /**
   * POST request helper
   */
  post<T>(endpoint: string, body: unknown, config?: Partial<RequestConfig>): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'POST',
      body,
      ...config,
    });
  }

  /**
   * GET request helper
   */
  get<T>(endpoint: string, config?: Partial<RequestConfig>): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'GET',
      ...config,
    });
  }
}

// Initialize and export singleton
const apiBaseUrl = process.env.NEXT_PUBLIC_API_URL;
if (!apiBaseUrl) {
  console.error(
    'NEXT_PUBLIC_API_URL is not set. API client will not work properly.'
  );
}

export const apiClient = new ApiClient(
  apiBaseUrl || 'http://localhost:8080'
);
