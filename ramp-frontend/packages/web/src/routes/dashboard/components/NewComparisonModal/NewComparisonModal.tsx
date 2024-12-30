import CloseIcon from '@mui/icons-material/Close';
import LoadingButton from '@mui/lab/LoadingButton';
import {
  Box,
  FormControl,
  IconButton,
  MenuItem,
  Modal,
  TextField,
  Typography,
} from '@mui/material';
import { useQueryClient } from '@tanstack/react-query';
import useFormResolver from 'hooks/useFormResolver';
import useNotification from 'hooks/useNotification';
import { FC, useState } from 'react';
import { FieldValues, SubmitHandler, useForm } from 'react-hook-form';
import { useIntl } from 'react-intl';
import { useAddComparison } from 'services/comparisonService';
import { useGetResearcherList } from 'services/researcherService';
import { ComparisonCreateRequestSchema } from 'types/comparisonTypes';

type Props = {
  isOpen: boolean;
  handleClose: () => void;
};

const NewComparisonModal: FC<Props> = ({ isOpen, handleClose }) => {
  const intl = useIntl();
  const formResolver = useFormResolver();
  const { showSuccess } = useNotification();
  const queryClient = useQueryClient();

  const [list1Selected, setList1Selected] = useState(0);
  const [list2Selected, setList2Selected] = useState(0);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: formResolver(ComparisonCreateRequestSchema),
  });

  const { data: researcherListResponse } = useGetResearcherList();

  const researcherList = researcherListResponse?.data;

  const { mutateAsync: addComparison, isLoading } = useAddComparison({
    onSuccess: async () => {
      showSuccess(intl.formatMessage({ id: 'success.researcherListAdded' }));
      queryClient.invalidateQueries({ queryKey: ['comparisons'] });
      handleClose();
    },
  });

  const onFormSubmit: SubmitHandler<FieldValues> = async ({ name }, e) => {
    await addComparison({
      name,
      list1: researcherList![list1Selected].id,
      list2: researcherList![list2Selected].id,
    });
  };

  return (
    <Modal open={isOpen} onClose={handleClose}>
      <Box
        sx={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)',
          width: 400,
          bgcolor: 'background.paper',
          boxShadow: 24,
          p: 4,
          borderRadius: 2,
        }}
      >
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
          <Typography variant="h6">{intl.formatMessage({ id: 'comparison.addNew' })}</Typography>
          <IconButton onClick={handleClose}>
            <CloseIcon />
          </IconButton>
        </Box>
        <Typography variant="body2" color="text.secondary" mb={2}>
          {intl.formatMessage({ id: 'info.newComparisonModal' })}
        </Typography>
        <form onSubmit={handleSubmit(onFormSubmit)}>
          <FormControl error={!!errors.name} fullWidth>
            <TextField
              fullWidth
              label={intl.formatMessage({ id: 'comparison.name' })}
              variant="outlined"
              id="name"
              //   disabled={isLoading}
              error={!!errors.name}
              inputProps={{
                ...register('name'),
              }}
              helperText={errors.name && `* ${errors.name.message}`}
            />
            <TextField
              fullWidth
              id="list1"
              select
              value={list1Selected}
              label={intl.formatMessage({ id: 'researcher.list' }) + ' 1'}
              defaultValue={0}
              onChange={(e) => setList1Selected(parseInt(e.target.value))}
            >
              {researcherList &&
                researcherList.map((option, index) => (
                  <MenuItem key={option.id} value={index}>
                    {option.name}
                  </MenuItem>
                ))}
            </TextField>
            <TextField
              fullWidth
              id="list1"
              select
              value={list2Selected}
              label={intl.formatMessage({ id: 'researcher.list' }) + ' 2'}
              defaultValue={0}
              onChange={(e) => setList2Selected(parseInt(e.target.value))}
            >
              {researcherList &&
                researcherList.map((option, index) => (
                  <MenuItem key={option.id} value={index}>
                    {option.name}
                  </MenuItem>
                ))}
            </TextField>
            <LoadingButton
              disabled={isLoading}
              loading={isLoading}
              variant="contained"
              type="submit"
              data-testid="add-button"
            >
              {intl.formatMessage({ id: 'generic.add' })}
            </LoadingButton>
          </FormControl>
        </form>
      </Box>
    </Modal>
  );
};

export default NewComparisonModal;
