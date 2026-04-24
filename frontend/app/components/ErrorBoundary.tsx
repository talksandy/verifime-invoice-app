'use client';

import React, { ReactNode } from 'react';
import { Alert, Box, Button } from '@mui/material';
import { BUTTON_LABELS } from '../constants';

interface Props {
  children: ReactNode;
}

interface State {
  hasError: boolean;
  error: Error | null;
}

export default class ErrorBoundary extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = { hasError: false, error: null };
  }

  static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    // Log error to external service (e.g., Sentry)
    console.error('Error caught by boundary:', error, errorInfo);
  }

  handleReset = () => {
    this.setState({ hasError: false, error: null });
  };

  render() {
    if (this.state.hasError) {
      return (
        <Box sx={{ p: 4 }}>
          <Alert severity="error" sx={{ mb: 2 }}>
            <strong>Something went wrong</strong>
            <p>{this.state.error?.message}</p>
          </Alert>
          <Button variant="contained" onClick={this.handleReset}>
            {BUTTON_LABELS.TRY_AGAIN}
          </Button>
        </Box>
      );
    }

    return this.props.children;
  }
}
