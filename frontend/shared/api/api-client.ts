import { getApiUrl } from '../config/env';

interface RequestConfig {
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
  headers?: Record<string, string>;
  body?: unknown;
  timeout?: number;
  retryAttempts?: number;
}

const DEFAULT_TIMEOUT = 30000;
const DEFAULT_RETRY_ATTEMPTS = 3;
const RETRY_DELAY = 1000;

class ApiClient {
  constructor(private readonly baseUrl: string) {}

  async request<T>(endpoint: string, config: RequestConfig = {}): Promise<T> {
    const {
      method = 'GET',
      headers = {},
      body,
      timeout = DEFAULT_TIMEOUT,
      retryAttempts = DEFAULT_RETRY_ATTEMPTS,
    } = config;

    let lastError: Error | null = null;

    for (let attempt = 0; attempt < retryAttempts; attempt += 1) {
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

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(
            `HTTP ${response.status}: ${errorText || response.statusText}`
          );
        }

        const contentType = response.headers.get('content-type');
        if (contentType?.includes('application/json')) {
          return await response.json();
        }

        return (await response.text()) as T;
      } catch (error) {
        lastError = error instanceof Error ? error : new Error(String(error));

        if (attempt < retryAttempts - 1) {
          const delay = RETRY_DELAY * Math.pow(2, attempt);
          console.warn(
            `Request failed (attempt ${attempt + 1}/${retryAttempts}): ${lastError.message}. Retrying in ${delay}ms...`
          );
          await this.sleep(delay);
        }
      }
    }

    throw new Error(
      `Request failed after ${retryAttempts} attempts: ${lastError?.message}`
    );
  }

  post<T>(
    endpoint: string,
    body: unknown,
    config?: Partial<RequestConfig>
  ): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'POST',
      body,
      ...config,
    });
  }

  get<T>(endpoint: string, config?: Partial<RequestConfig>): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'GET',
      ...config,
    });
  }

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
            return;
          }

          reject(error);
        })
        .finally(() => clearTimeout(timeoutId));
    });
  }

  private sleep(ms: number): Promise<void> {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }
}

export const apiClient = new ApiClient(getApiUrl());
