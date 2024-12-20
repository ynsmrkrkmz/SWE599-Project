import getRandomInt from '@ramp/utils/getRandomInt';
import HelpIcon from '@mui/icons-material/Help';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import Logout from '@mui/icons-material/Logout';
import NotificationsIcon from '@mui/icons-material/Notifications';
import Settings from '@mui/icons-material/Settings';
import Avatar from '@mui/material/Avatar';
import Badge from '@mui/material/Badge';
import Box from '@mui/material/Box';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Stack from '@mui/material/Stack';
import { useTheme } from '@mui/material/styles';
import Toolbar from '@mui/material/Toolbar';
import Tooltip from '@mui/material/Tooltip';
import Typography from '@mui/material/Typography';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useAppContext } from 'contexts/AppContext';
import { useAuthContext } from 'contexts/AuthContext';
import React, { FC, useEffect } from 'react';
import { useIntl } from 'react-intl';
import { Link, useNavigate } from 'react-router-dom';
import { AppBar } from './Header.style';
import { Link as MuiLink } from '@mui/material';
import { BsSendArrowDownFill } from 'react-icons/bs';
import { stringAvatar } from 'utils/generateAvatarFromName';

const Header: FC = () => {
  const intl = useIntl();
  const theme = useTheme();
  const { user, signOut } = useAuthContext();
  const matches = useMediaQuery(theme.breakpoints.up('xl'));
  const [open, setOpen] = React.useState(matches);
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const isMenuOpen = Boolean(anchorEl);
  const navigate = useNavigate();

  useEffect(() => {
    setOpen(matches);
  }, [matches]);

  const handleMenuOpen = (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget);
  };
  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  return (
    <AppBar color="transparent" sx={{ boxShadow: 'none', background: '#F4F5FA' }}>
      <Toolbar>
        <MuiLink onClick={() => navigate('/')} underline="none" sx={{ cursor: 'pointer' }}>
          <Typography variant={'h1'} color="primary">
            ramp
          </Typography>
        </MuiLink>
        <Box flexGrow={1} justifyContent={'space-between'}></Box>

        <Stack spacing={1} direction="row" sx={{ mr: 3 }}>
          {/* <Tooltip title={intl.formatMessage({ id: 'generic.help' })}>
            <IconButton component={Link} to="/help">
              <HelpIcon color="action" />
            </IconButton>
          </Tooltip>
          <Divider orientation="vertical" flexItem />
          <Tooltip title={intl.formatMessage({ id: 'generic.notifications' })}>
            <IconButton component={Link} to="/notifications">
              <Badge badgeContent={5} max={99} color="error">
                <NotificationsIcon color="action" />
              </Badge>
            </IconButton>
          </Tooltip> */}
          <Divider orientation="vertical" flexItem />
          {user && (
            <Stack spacing={1} direction={'row'} alignItems={'center'}>
              <Avatar alt="User Avatar" {...stringAvatar(`${user.name} ${user.lastname}`)} />

              <Typography>
                {user?.name} {user?.lastname}
              </Typography>
              <IconButton
                onClick={handleMenuOpen}
                size="small"
                aria-controls={isMenuOpen ? 'account-menu' : undefined}
                aria-haspopup="true"
                aria-expanded={isMenuOpen ? 'true' : undefined}
              >
                <KeyboardArrowDownIcon fontSize="small" />
              </IconButton>
              <Menu
                anchorEl={anchorEl}
                id="account-menu"
                open={isMenuOpen}
                onClose={handleMenuClose}
                onClick={handleMenuClose}
                transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
              >
                <MenuItem component={Link} to="/user/profile">
                  <ListItemIcon>
                    <Settings fontSize="small" />
                  </ListItemIcon>
                  {intl.formatMessage({ id: 'user.profile' })}
                </MenuItem>
                <MenuItem component={Link} to="/user/invitations">
                  <ListItemIcon>
                    <BsSendArrowDownFill fontSize="large" />
                  </ListItemIcon>
                  {intl.formatMessage({ id: 'user.invitations' })}
                </MenuItem>
                <MenuItem onClick={signOut}>
                  <ListItemIcon>
                    <Logout fontSize="small" />
                  </ListItemIcon>
                  {intl.formatMessage({ id: 'generic.signOut' })}
                </MenuItem>
              </Menu>
            </Stack>
          )}
        </Stack>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
