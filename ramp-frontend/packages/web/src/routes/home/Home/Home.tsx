import { Divider, Stack, Typography } from '@mui/material';
import ContentLoading from 'components/ContentLoading';
import { useAppContext } from 'contexts/AppContext';
import { FC, useEffect } from 'react';
import { useIntl } from 'react-intl';

const Home: FC = () => {
  const intl = useIntl();
  const { setPageName } = useAppContext();

  useEffect(() => {
    setPageName(intl.formatMessage({ id: 'generic.home' }));
  }, [intl, setPageName]);

  const isLoading = false;

  return isLoading ? (
    <ContentLoading />
  ) : (
    <>
      <Stack direction={'row'}>
        <Typography variant={'h4'} color="primary" sx={{ flexGrow: 1 }}>
          {intl.formatMessage({
            id: 'generic.home',
          })}
        </Typography>
      </Stack>
      <Divider />
      <Stack direction={'column'} spacing={1}></Stack>
    </>
  );
};

export { Home };
