import LoadingButton from '@mui/lab/LoadingButton';
import {
  Box,
  Card,
  CardContent,
  CardHeader,
  Divider,
  FormControl,
  Grid,
  Link,
  Stack,
  TextField,
  Typography,
} from '@mui/material';
import PasswordField from 'components/PasswordField';
import { useAppContext } from 'contexts/AppContext';
import useFormResolver from 'hooks/useFormResolver';
import useNotification from 'hooks/useNotification';
import { FC, useEffect } from 'react';
import { FieldValues, useForm } from 'react-hook-form';
import { useIntl } from 'react-intl';
import { useNavigate } from 'react-router-dom';
import { useSignup } from 'services/userService';
import { SignupFormInputSchema } from 'types/userTypes';
import zxcvbn from 'zxcvbn';

const Signup: FC = () => {
  const intl = useIntl();
  const { language } = useAppContext();
  const navigate = useNavigate();
  const { showSuccess } = useNotification();
  const formResolver = useFormResolver();

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm({
    resolver: formResolver(SignupFormInputSchema),
  });

  const { mutateAsync: signupMutate, isLoading, isSuccess } = useSignup();

  useEffect(() => {
    if (!isLoading && isSuccess) {
      navigate('/');
    }
  }, [isLoading, navigate, isSuccess]);

  const onSubmit = async ({ name, lastname, email, password, confirm }: FieldValues) => {
    const { meta } = await signupMutate({ name, lastname, email, password, confirm, role: 1 });

    if (isSuccess) showSuccess(meta?.message);
  };

  return (
    <Stack spacing={2} alignItems={'center'} paddingBottom={5}>
      <Card>
        <CardContent>
          <CardHeader
            align="center"
            title={intl.formatMessage({
              id: 'signupForm.title',
            })}
            subheader={intl.formatMessage({
              id: 'signupForm.subTitle',
            })}
          />

          <form onSubmit={handleSubmit(onSubmit)}>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <FormControl fullWidth error={!!errors.name}>
                  <TextField
                    id="name"
                    label={intl.formatMessage({ id: 'generic.name' })}
                    disabled={isLoading}
                    error={!!errors.name}
                    inputProps={{
                      ...register('name'),
                      'data-testid': 'name-field',
                    }}
                    helperText={errors.name && `* ${errors.name.message}`}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={12}>
                <FormControl fullWidth error={!!errors.lastname}>
                  <TextField
                    id="lastname"
                    label={intl.formatMessage({ id: 'generic.lastname' })}
                    disabled={isLoading}
                    error={!!errors.lastname}
                    inputProps={{
                      ...register('lastname'),
                      'data-testid': 'lastname-field',
                    }}
                    helperText={errors.lastname && `* ${errors.lastname.message}`}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={12}>
                <FormControl fullWidth error={!!errors.email}>
                  <TextField
                    id="email"
                    label={intl.formatMessage({ id: 'generic.email' })}
                    disabled={isLoading}
                    error={!!errors.email}
                    inputProps={{
                      ...register('email'),
                      'data-testid': 'email-field',
                    }}
                    helperText={errors.email && `* ${errors.email.message}`}
                  />
                </FormControl>
              </Grid>
              <Grid item xs={12}>
                <PasswordField
                  id="password"
                  fullWidth
                  showStrengthBar
                  strengthScore={watch('password') ? zxcvbn(watch('password')).score : 0}
                  label={intl.formatMessage({ id: 'generic.password' })}
                  disabled={isLoading}
                  error={!!errors.password}
                  inputProps={{ ...register('password'), 'data-testid': 'password-field' }}
                  helperText={errors.password && `* ${errors.password.message}`}
                />
              </Grid>
              <Grid item xs={12}>
                <PasswordField
                  id="confirm"
                  fullWidth
                  label={intl.formatMessage({ id: 'generic.confirmPassword' })}
                  disabled={isLoading}
                  error={!!errors.confirm}
                  inputProps={{ ...register('confirm'), 'data-testid': 'confirm-field' }}
                  helperText={errors.confirm && `* ${errors.confirm.message}`}
                />
              </Grid>
              <Grid item xs={12}>
                <LoadingButton
                  fullWidth
                  disabled={isLoading}
                  loading={isLoading}
                  variant="contained"
                  type="submit"
                  data-testid="sign-up-button"
                >
                  {intl.formatMessage({ id: 'generic.signup' })}
                </LoadingButton>
              </Grid>
            </Grid>
          </form>
        </CardContent>
      </Card>
      <Box width={'100%'} paddingX={3}>
        <Divider />
      </Box>
      <Stack direction={'row'} spacing={1}>
        {intl.formatMessage({
          id: 'signupform.alreadyHaveAccount',
        })}
        <Link
          onClick={() => {
            navigate('/login');
          }}
        >
          <Typography>
            {intl.formatMessage({
              id: 'generic.login',
            })}
          </Typography>
        </Link>
      </Stack>
    </Stack>
  );
};

export default Signup;
