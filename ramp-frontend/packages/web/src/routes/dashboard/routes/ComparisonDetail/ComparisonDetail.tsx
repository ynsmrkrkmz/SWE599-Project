import SyncIcon from '@mui/icons-material/Sync';
import LoadingButton from '@mui/lab/LoadingButton';
import { Stack, Typography } from '@mui/material';
import { useQueryClient } from '@tanstack/react-query';
import ContentLoading from 'components/ContentLoading';
import useNotification from 'hooks/useNotification';
import { FC } from 'react';
import { useIntl } from 'react-intl';
import { useParams } from 'react-router-dom';
import { useGetComparisonDetail, useUpdateComparisonData } from 'services/comparisonService';
import ComparisonCitationDetailsCard from '../../components/ComparisonCitationDetailsCard';
import ComparisonTopWorksDetailsCard from '../../components/ComparisonTopWorksDetailsCard';
import Root from './ComparisonDetail.style';

const Comparison: FC = () => {
  const intl = useIntl();
  const { dashboardId } = useParams();
  const { showSuccess } = useNotification();
  const queryClient = useQueryClient();

  const { data, isLoading } = useGetComparisonDetail(dashboardId);

  const { mutateAsync: updateComparison, isLoading: isUpdateComparisonLoading } =
    useUpdateComparisonData({
      onSuccess: async () => {
        showSuccess(intl.formatMessage({ id: 'success.comparisonDetailsUpdated' }));
        queryClient.invalidateQueries({ queryKey: ['comparison-detail', dashboardId] });
      },
    });

  const comparisonDetails = data?.data;

  const handleUpdateComparison = async () => {
    dashboardId && (await updateComparison({ comparisonId: dashboardId }));
  };

  if (isLoading) {
    return <ContentLoading />;
  }

  return (
    <Root>
      <Stack direction={'row'}>
        <Typography variant={'h1'} color="primary" sx={{ flexGrow: 1 }}>
          {intl.formatMessage({
            id: 'comparison.detail',
          })}
        </Typography>
        <LoadingButton
          variant="contained"
          onClick={handleUpdateComparison}
          startIcon={<SyncIcon />}
          disabled={isUpdateComparisonLoading}
          loading={isUpdateComparisonLoading}
        >
          <Typography>{intl.formatMessage({ id: 'comparison.syncData' })}</Typography>
        </LoadingButton>
      </Stack>

      {comparisonDetails && (
        <>
          <ComparisonCitationDetailsCard
            id={comparisonDetails.id}
            list1Analysis={comparisonDetails.list1Analysis}
            list2Analysis={comparisonDetails.list2Analysis}
            name={comparisonDetails.name}
          />

          <ComparisonTopWorksDetailsCard
            id={comparisonDetails.id}
            list1Analysis={comparisonDetails.list1Analysis}
            list2Analysis={comparisonDetails.list2Analysis}
            name={comparisonDetails.name}
          />
        </>
      )}
    </Root>
  );
};

export default Comparison;
