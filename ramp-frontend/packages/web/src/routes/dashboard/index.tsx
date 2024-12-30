import { useAppContext } from 'contexts/AppContext';
import { FC, useEffect } from 'react';
import { useIntl } from 'react-intl';
import { Route, Routes } from 'react-router-dom';
import Comparison from './routes/Comparison';
import ComparisonDetail from './routes/ComparisonDetail';

const DasboardRoute: FC = () => {
  const { setPageName } = useAppContext();
  const intl = useIntl();

  useEffect(() => {
    setPageName(intl.formatMessage({ id: 'generic.dashboard' }));
  }, [intl, setPageName]);

  return (
    <Routes>
      <Route index element={<Comparison />} />
      <Route path=":dashboardId" element={<ComparisonDetail />}></Route>
    </Routes>
  );
};

export { DasboardRoute };
