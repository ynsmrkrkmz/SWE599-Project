import AddIcon from '@mui/icons-material/Add';
import { Button, Grid, Stack, Typography } from '@mui/material';
import ContentLoading from 'components/ContentLoading';
import { FC, useState } from 'react';
import { useIntl } from 'react-intl';
import { useGetResearcherList } from 'services/researcherService';
import ResearcherListCard from '../../components/ResearcherListCard/ResearcherListCard';
import Root from './ResearcherList.style';
import NewResearcherListModal from '../../components/NewResearcherListModal';

const ResearcherList: FC = () => {
  const intl = useIntl();
  const [isNewResearcherListModalOpen, setIsNewResearcherListModalOpen] = useState(false);

  const { data, isLoading } = useGetResearcherList();

  const researcherList = data?.data;

  const handleNewResearcherList = () => {
    setIsNewResearcherListModalOpen(true);
  };

  return (
    <Root>
      <Stack direction={'row'}>
        <Typography variant={'h1'} color="primary" sx={{ flexGrow: 1 }}>
          {intl.formatMessage({
            id: 'researcher.list',
          })}
        </Typography>
        <Button variant="contained" onClick={handleNewResearcherList} startIcon={<AddIcon />}>
          <Typography>{intl.formatMessage({ id: 'researcher.newList' })}</Typography>
        </Button>
      </Stack>

      {isLoading ? (
        <ContentLoading />
      ) : (
        <Grid container spacing={1}>
          {researcherList && researcherList.length > 0 ? (
            researcherList.map((c) => (
              <Grid key={c.id} item>
                <ResearcherListCard id={c.id} name={c.name} />
              </Grid>
            ))
          ) : (
            <Typography variant={'subtitle1'} color="primary" sx={{ flexGrow: 1 }} padding={'10px'}>
              {intl.formatMessage({
                id: 'info.noValueFound',
              })}
            </Typography>
          )}
        </Grid>
      )}
      <NewResearcherListModal
        isOpen={isNewResearcherListModalOpen}
        handleClose={() => setIsNewResearcherListModalOpen(false)}
      />
    </Root>
  );
};

export default ResearcherList;
