import { useAppContext } from 'contexts/AppContext';
import { FC, useEffect } from 'react';
import { useIntl } from 'react-intl';
import { Route, Routes } from 'react-router-dom';
import ResearcherList from './routes/ResearcherList';
import ResearcherListDetail from './components/ResearcherListDetail';

const ResearcherListRoute: FC = () => {
  const { setPageName } = useAppContext();
  const intl = useIntl();

  useEffect(() => {
    setPageName(intl.formatMessage({ id: 'researcher.list' }));
  }, [intl, setPageName]);

  return (
    <Routes>
      <Route index element={<ResearcherList />} />
      <Route path=":researcherListId" element={<ResearcherListDetail />}></Route>
    </Routes>
  );
};

export { ResearcherListRoute };
