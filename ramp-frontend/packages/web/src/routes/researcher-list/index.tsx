import { useAppContext } from 'contexts/AppContext';
import { FC, useEffect } from 'react';
import { useIntl } from 'react-intl';
import { Route, Routes } from 'react-router-dom';
import ResearcherList from './routes/ResearcherList';

const ResearcherListRoute: FC = () => {
  const { setPageName } = useAppContext();
  const intl = useIntl();

  useEffect(() => {
    setPageName(intl.formatMessage({ id: 'researcher.list' }));
  }, [intl, setPageName]);

  return (
    <Routes>
      <Route index element={<ResearcherList />} />
      <Route path=":researcherListId" element={'Reseacrher List Details'}></Route>
    </Routes>
  );
};

export { ResearcherListRoute };
