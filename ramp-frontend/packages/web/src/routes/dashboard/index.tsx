import { useAppContext } from 'contexts/AppContext';
import { FC, useEffect } from 'react';
import { useIntl } from 'react-intl';
import { Route, Routes } from 'react-router-dom';

const DasboardRoute: FC = () => {
  const { setPageName } = useAppContext();
  const intl = useIntl();

  useEffect(() => {
    setPageName(intl.formatMessage({ id: 'generic.dashboard' }));
  }, [intl, setPageName]);

  return (
    <Routes>
      <Route index element={'Dasboard List'} />
      <Route path=":dashboardId" element={'Dasboard Details'}></Route>
    </Routes>
  );
};

export { DasboardRoute };
