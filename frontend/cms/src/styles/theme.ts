import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
    background: {
      default: '#f5f5f5',
    },
  },
  components: {
    MuiAppBar: {
      defaultProps: {
        color: 'default',
        elevation: 1,
      },
    },
    MuiDrawer: {
      defaultProps: {
        elevation: 1,
      },
    },
    MuiCard: {
      defaultProps: {
        elevation: 1,
      },
    },
  },
});
