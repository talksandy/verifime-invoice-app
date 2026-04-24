/**
 * Environment Variable Validation
 * Ensures all required environment variables are set and valid
 */

interface EnvironmentConfig {
  apiUrl: string;
}

/**
 * Validates and returns environment configuration
 * Should be called once at application startup
 */
export function validateEnvironment(): EnvironmentConfig {
  const apiUrl = process.env.NEXT_PUBLIC_API_URL;

  if (!apiUrl) {
    throw new Error(
      'NEXT_PUBLIC_API_URL environment variable is not set. ' +
      'Please configure it in your .env.local or deployment environment.'
    );
  }

  // Validate API URL format
  try {
    new URL(apiUrl);
  } catch {
    throw new Error(
      `NEXT_PUBLIC_API_URL is not a valid URL: "${apiUrl}". ` +
      'Please provide a valid URL (e.g., http://localhost:8080)'
    );
  }

  return {
    apiUrl,
  };
}

/**
 * Get environment configuration with fallback values
 * Use this for optional configuration
 */
export function getEnvironmentConfig(): EnvironmentConfig {
  try {
    return validateEnvironment();
  } catch (error) {
    if (typeof window === 'undefined') {
      // Server-side: throw error
      throw error;
    }
    // Client-side: use fallback
    console.warn('Using default API URL:', error);
    return {
      apiUrl: 'http://localhost:8080',
    };
  }
}

/**
 * Get API URL from environment
 */
export function getApiUrl(): string {
  const config = getEnvironmentConfig();
  return config.apiUrl;
}

/**
 * Check if running in production
 */
export function isProduction(): boolean {
  return process.env.NODE_ENV === 'production';
}

/**
 * Check if running in development
 */
export function isDevelopment(): boolean {
  return process.env.NODE_ENV === 'development';
}
