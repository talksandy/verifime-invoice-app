interface EnvironmentConfig {
  apiUrl: string;
}

export function validateEnvironment(): EnvironmentConfig {
  const apiUrl = process.env.NEXT_PUBLIC_API_URL;

  if (!apiUrl) {
    throw new Error(
      'NEXT_PUBLIC_API_URL environment variable is not set. ' +
        'Please configure it in your .env.local or deployment environment.'
    );
  }

  try {
    new URL(apiUrl);
  } catch {
    throw new Error(
      `NEXT_PUBLIC_API_URL is not a valid URL: "${apiUrl}". ` +
        'Please provide a valid URL (e.g., http://localhost:8080)'
    );
  }

  return { apiUrl };
}

export function getEnvironmentConfig(): EnvironmentConfig {
  try {
    return validateEnvironment();
  } catch (error) {
    if (typeof window === 'undefined') {
      throw error;
    }

    console.warn('Using default API URL:', error);
    return {
      apiUrl: 'http://localhost:8080',
    };
  }
}

export function getApiUrl(): string {
  return getEnvironmentConfig().apiUrl;
}

export function isProduction(): boolean {
  return process.env.NODE_ENV === 'production';
}

export function isDevelopment(): boolean {
  return process.env.NODE_ENV === 'development';
}
