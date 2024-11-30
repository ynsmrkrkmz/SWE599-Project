import DashboardIcon from '@mui/icons-material/Dashboard';
import ViewListIcon from '@mui/icons-material/ViewList';
import { Box, List, ListItem, ListItemButton, ListItemIcon, ListItemText } from '@mui/material';
import { FC } from 'react';
import { useIntl } from 'react-intl';
import { useLocation, useNavigate } from 'react-router-dom';

const LeftMenu: FC = () => {
  const intl = useIntl();
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = (path: string) => location.pathname === path;

  console.log(location.pathname);

  const menuItems = [
    { label: 'generic.dashboard', icon: <DashboardIcon />, path: '/dashboard' },
    { label: 'researcher.list', icon: <ViewListIcon />, path: '/researcher-list' },
  ];

  return (
    <Box sx={{ minWidth: '256px', maxWidth: 360, height: '100vh' }}>
      <List disablePadding>
        {menuItems.map((item) => (
          <ListItem disablePadding>
            <ListItemButton
              onClick={() => navigate(`.${item.path}`)}
              selected={isActive(item.path)}
            >
              <ListItemIcon>{item.icon}</ListItemIcon>
              <ListItemText primary={intl.formatMessage({ id: item.label })} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Box>
  );
};

export default LeftMenu;
