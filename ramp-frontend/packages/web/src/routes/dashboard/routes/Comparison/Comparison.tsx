import AddIcon from '@mui/icons-material/Add';
import { Button, Grid, Stack, Typography } from '@mui/material';
import ContentLoading from 'components/ContentLoading';
import { FC, useState } from 'react';
import { useIntl } from 'react-intl';
import { useGetAllComparison } from 'services/comparisonService';
import ComparisonCard from '../../components/ComparisonCard/ComparisonCard';
import NewComparisonModal from '../../components/NewComparisonModal';
import Root from './Comparison.style';

const Comparison: FC = () => {
  const intl = useIntl();
  const [isNewComparisonModalOpen, setIsNewComparisonModalOpen] = useState(false);

  const { data, isLoading } = useGetAllComparison();

  const comparisons = data?.data;

  const handleComparison = () => {
    setIsNewComparisonModalOpen(true);
  };

  return (
    <Root>
      <Stack direction={'row'}>
        <Typography variant={'h1'} color="primary" sx={{ flexGrow: 1 }}>
          {intl.formatMessage({
            id: 'comparison.dashboard',
          })}
        </Typography>
        <Button variant="contained" onClick={handleComparison} startIcon={<AddIcon />}>
          <Typography>{intl.formatMessage({ id: 'comparison.addNew' })}</Typography>
        </Button>
      </Stack>

      {isLoading ? (
        <ContentLoading />
      ) : (
        <Grid container spacing={1}>
          {comparisons && comparisons.length > 0 ? (
            comparisons.map((c) => (
              <Grid key={c.id} item>
                <ComparisonCard id={c.id} name={c.name} />
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
      <NewComparisonModal
        isOpen={isNewComparisonModalOpen}
        handleClose={() => setIsNewComparisonModalOpen(false)}
      />
    </Root>
  );
};

export default Comparison;
