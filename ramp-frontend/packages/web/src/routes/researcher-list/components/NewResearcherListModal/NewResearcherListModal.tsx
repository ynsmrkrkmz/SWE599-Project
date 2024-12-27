import CloseIcon from '@mui/icons-material/Close';
import LoadingButton from '@mui/lab/LoadingButton';
import { Box, FormControl, IconButton, Modal, TextField, Typography } from '@mui/material';
import { useQueryClient } from '@tanstack/react-query';
import useFormResolver from 'hooks/useFormResolver';
import useNotification from 'hooks/useNotification';
import { FC } from 'react';
import { FieldValues, SubmitHandler, useForm } from 'react-hook-form';
import { useIntl } from 'react-intl';
import { useAddResearcherList } from 'services/researcherService';
import { ResearcherListCreateRequestSchema } from 'types/researcherTypes';

type Props = {
  isOpen: boolean;
  handleClose: () => void;
};

const NewResearcherListModal: FC<Props> = ({ isOpen, handleClose }) => {
  const intl = useIntl();
  const formResolver = useFormResolver();
  const { showSuccess } = useNotification();
  const queryClient = useQueryClient();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: formResolver(ResearcherListCreateRequestSchema),
  });

  const { mutateAsync: addNewList, isLoading } = useAddResearcherList({
    onSuccess: async () => {
      showSuccess(intl.formatMessage({ id: 'success.researcherListAdded' }));
      queryClient.invalidateQueries({ queryKey: ['researcher-list'] });
      handleClose();
    },
  });

  const onFormSubmit: SubmitHandler<FieldValues> = async ({ name }, e) => {
    await addNewList({ name });
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
          <Typography variant="h6">{intl.formatMessage({ id: 'researcher.newList' })}</Typography>
          <IconButton onClick={handleClose}>
            <CloseIcon />
          </IconButton>
        </Box>
        <Typography variant="body2" color="text.secondary" mb={2}>
          {intl.formatMessage({ id: 'info.newResearcherListModal' })}
        </Typography>
        <form onSubmit={handleSubmit(onFormSubmit)}>
          <FormControl error={!!errors.name} fullWidth>
            <TextField
              fullWidth
              label={intl.formatMessage({ id: 'researcher.listName' })}
              variant="outlined"
              id="name"
              //   disabled={isLoading}
              error={!!errors.name}
              inputProps={{
                ...register('name'),
              }}
              helperText={errors.name && `* ${errors.name.message}`}
            />
            <LoadingButton
              disabled={isLoading}
              loading={isLoading}
              variant="contained"
              type="submit"
              data-testid="invite-button"
            >
              {intl.formatMessage({ id: 'generic.add' })}
            </LoadingButton>
          </FormControl>
        </form>
      </Box>
    </Modal>
  );
};

export default NewResearcherListModal;
