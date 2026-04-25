import { apiClient } from '../api-client';

describe('apiClient', () => {
  beforeEach(() => {
    vi.restoreAllMocks();
    vi.spyOn(console, 'warn').mockImplementation(() => {});
  });

  afterEach(() => {
    vi.useRealTimers();
  });

  it('returns parsed JSON for successful requests', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue({
        ok: true,
        headers: new Headers({ 'content-type': 'application/json' }),
        json: vi.fn().mockResolvedValue({ success: true }),
      })
    );

    await expect(apiClient.get('/test')).resolves.toEqual({ success: true });
  });

  it('converts abort errors into timeout errors', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn().mockRejectedValue(
        Object.assign(new Error('Aborted'), { name: 'AbortError' })
      )
    );

    await expect(
      apiClient.get('/test', { retryAttempts: 1, timeout: 50 })
    ).rejects.toThrow('Request failed after 1 attempts: Request timeout after 50ms');
  });

  it('retries failed requests before succeeding', async () => {
    vi.useFakeTimers();

    let attemptCount = 0;
    vi.stubGlobal(
      'fetch',
      vi.fn().mockImplementation(() => {
        attemptCount += 1;

        if (attemptCount < 3) {
          return Promise.reject(new Error('Network error'));
        }

        return Promise.resolve({
          ok: true,
          headers: new Headers({ 'content-type': 'application/json' }),
          json: vi.fn().mockResolvedValue({ success: true }),
        });
      })
    );

    const request = apiClient.get('/test');
    await vi.runAllTimersAsync();

    await expect(request).resolves.toEqual({ success: true });
    expect(attemptCount).toBe(3);
  });
});
