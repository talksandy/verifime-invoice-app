import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  // Compression and performance optimization
  compress: true,
  
  // Remove powered by header for security
  poweredByHeader: false,
  
  // Optimization for production build size
  productionBrowserSourceMaps: false,
  
  // Security headers
  async headers() {
    return [
      {
        source: "/:path*",
        headers: [
          {
            key: "X-Content-Type-Options",
            value: "nosniff",
          },
          {
            key: "X-Frame-Options",
            value: "DENY",
          },
          {
            key: "X-XSS-Protection",
            value: "1; mode=block",
          },
          {
            key: "Referrer-Policy",
            value: "strict-origin-when-cross-origin",
          },
        ],
      },
    ];
  },

  // Strict mode for development
  reactStrictMode: true,
};

export default nextConfig;
