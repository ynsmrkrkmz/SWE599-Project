import React, { useMemo, useState } from 'react';
import {
  Box,
  Typography,
  TextField,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  FormControl,
  IconButton,
} from '@mui/material';
import { GridActionsCellItem, GridColumns } from '@mui/x-data-grid';
import { useIntl } from 'react-intl';
import { useParams } from 'react-router-dom';
import {
  useAddResearcherListMembership,
  useGetResearcherListDetail,
  useRemoveResearcherListMembership,
  useSearchAuthor,
} from 'services/researcherService';
import { PiTrashBold } from 'react-icons/pi';
import ConfirmationDialog from 'components/ComfirmationDialog';
import { StyledDataGrid } from './ResearcherListDetail.style';
import LoadingButton from '@mui/lab/LoadingButton';
import { FieldValues, SubmitHandler, useForm } from 'react-hook-form';
import useFormResolver from 'hooks/useFormResolver';
import {
  Researcher,
  ResearcherListMembershipCreateRequest,
  ResearcherSearchSchema,
} from 'types/researcherTypes';
import ContentLoading from 'components/ContentLoading';
import useNotification from 'hooks/useNotification';
import ClearIcon from '@mui/icons-material/Clear';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import AddIcon from '@mui/icons-material/Add';
import { useQueryClient } from '@tanstack/react-query';

const ResearcherListDetail = () => {
  const intl = useIntl();
  const { researcherListId } = useParams();
  const formResolver = useFormResolver();
  const queryClient = useQueryClient();
  const { showSuccess } = useNotification();

  const { data, isLoading } = useGetResearcherListDetail(researcherListId);

  const { mutateAsync: searchAuthor, isLoading: isSearchLoading } = useSearchAuthor();
  const { mutateAsync: addResearcher, isLoading: isAddingResearcherLoading } =
    useAddResearcherListMembership({
      onSuccess: async () => {
        showSuccess(intl.formatMessage({ id: 'success.researcherAddedToList' }));
        queryClient.invalidateQueries({ queryKey: ['researcher-list-detail'] });
      },
    });
  const { mutateAsync: removeResearcher, isLoading: isRemovingResearcherLoading } =
    useRemoveResearcherListMembership({
      onSuccess: async () => {
        showSuccess(intl.formatMessage({ id: 'success.researcherRemovedFromList' }));
        queryClient.invalidateQueries({ queryKey: ['researcher-list-detail'] });
        handleDialogClose();
      },
    });

  const researcherListDetail = data?.data;

  const [pageSize, setPageSize] = useState<number>(10);
  const [selectedResearcherId, setSelectedResearcherId] = useState<string | null>(null);
  const [dialogRemoveResearcherOpen, setDialogemoveResearcheOpen] = useState<boolean>(false);
  const [results, setResults] = useState<Researcher[]>([]);
  const [showResults, setShowResults] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');

  const handleDialogOpen = (id: string) => () => {
    setDialogemoveResearcheOpen(true);
    setSelectedResearcherId(id);
  };

  const handleDialogClose = () => {
    setDialogemoveResearcheOpen(false);
    setSelectedResearcherId(null);
  };

  const handleConfirm = async () => {
    selectedResearcherId &&
      researcherListId &&
      (await removeResearcher({ researcherId: selectedResearcherId, listId: researcherListId }));
  };

  const handleAdd = async (openAlexId: string) => {
    const researcher = results.find((r) => r.openAlexId === openAlexId);

    if (researcher) {
      const resarcherListMembership: ResearcherListMembershipCreateRequest = {
        researcherListId: +researcherListId!,
        openAlexId: researcher.openAlexId,
        orcId: researcher.orcId,
        name: researcher.name,
        institution: researcher.institution,
        institutionCountry: researcher.institutionCountry,
      };

      await addResearcher(resarcherListMembership);
    }
  };

  const handleClear = () => {
    setSearchTerm('');
    setResults([]);
    setShowResults(false); // Hide results overlay
  };

  const isAdded = (openAlexId: string) => gridData.some((r) => r.openAlexId === openAlexId);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: formResolver(ResearcherSearchSchema),
  });

  const onFormSubmit: SubmitHandler<FieldValues> = async ({ researcherName }, e) => {
    const { data } = await searchAuthor(researcherName);
    data && setResults(data);
    setShowResults(true);
  };

  const columns: GridColumns = useMemo(
    () => [
      {
        field: 'openAlexId',
        headerName: intl.formatMessage({
          id: 'generic.openAlexId',
        }),
        minWidth: 200,
      },
      {
        field: 'name',
        headerName: intl.formatMessage({
          id: 'researcher.name',
        }),
        minWidth: 200,
        flex: 1,
      },
      {
        field: 'institution',
        headerName: intl.formatMessage({ id: 'generic.institution' }),
        width: 220,
        flex: 1,
      },
      {
        field: 'institutionCountry',
        headerName: intl.formatMessage({ id: 'generic.country' }),
        width: 150,
      },
      {
        field: 'actions',
        type: 'actions',
        width: 80,
        getActions: (params) => [
          <GridActionsCellItem
            key={params.id}
            disabled={false}
            icon={<PiTrashBold />}
            label={'Remove'}
            onClick={handleDialogOpen(params.id as string)}
          />,
        ],
      },
    ],
    [intl]
  );

  const gridData = useMemo(() => {
    if (data?.data) {
      return data.data.researchers;
    }

    return [];
  }, [data?.data]);

  if (isLoading) {
    return <ContentLoading />;
  }

  return (
    <Box sx={{ width: '100%' }}>
      <Typography variant="h5" gutterBottom>
        {intl.formatMessage({ id: 'researcher.listDetail' })}
      </Typography>

      {/* List Name Input */}
      <Box sx={{ mb: 3 }}>
        <TextField
          fullWidth
          label={intl.formatMessage({ id: 'researcher.listName' })}
          variant="outlined"
          defaultValue={researcherListDetail?.name}
          InputLabelProps={{
            shrink: true,
          }}
          InputProps={{
            readOnly: true,
          }}
        />
      </Box>

      {/* Researcher Name Input with Button */}
      <form onSubmit={handleSubmit(onFormSubmit)}>
        <FormControl error={!!errors.name} fullWidth>
          <Box sx={{ display: 'flex', alignItems: 'start', gap: '16px' }}>
            <TextField
              fullWidth
              label={intl.formatMessage({ id: 'researcher.name' })}
              variant="outlined"
              id="researcherName"
              //   disabled={isLoading}
              error={!!errors.name}
              inputProps={{
                ...register('researcherName'),
              }}
              helperText={errors.name && `* ${errors.name.message}`}
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              InputProps={{
                endAdornment: searchTerm && (
                  <IconButton disabled={isSearchLoading} onClick={handleClear}>
                    <ClearIcon />
                  </IconButton>
                ),
              }}
              disabled={isSearchLoading}
            />
            <LoadingButton
              disabled={isSearchLoading}
              loading={isSearchLoading}
              variant="contained"
              type="submit"
              data-testid="search-button"
              style={{ marginTop: '6px' }}
            >
              {intl.formatMessage({ id: 'generic.search' })}
            </LoadingButton>
          </Box>
        </FormControl>
      </form>

      {/* Result Overlay */}
      {showResults && (
        <Paper
          elevation={3}
          sx={{
            position: 'absolute',
            left: 320,
            right: 144,
            zIndex: 1000,
            maxHeight: 400, // Restrict height
            overflow: 'auto', // Enable scrolling
            border: '1px solid #ddd',
            p: 2,
            backgroundColor: 'white',
            marginTop: '-19px',
          }}
        >
          <TableContainer>
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell>
                    <strong>
                      {intl.formatMessage({
                        id: 'generic.openAlexId',
                      })}
                    </strong>
                  </TableCell>
                  <TableCell>
                    <strong>
                      {intl.formatMessage({
                        id: 'researcher.name',
                      })}
                    </strong>
                  </TableCell>
                  <TableCell>
                    <strong>
                      {intl.formatMessage({
                        id: 'generic.institution',
                      })}
                    </strong>
                  </TableCell>
                  <TableCell>
                    <strong>
                      {intl.formatMessage({
                        id: 'generic.country',
                      })}
                    </strong>
                  </TableCell>
                  <TableCell></TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {results.length > 0 ? (
                  results.map((result) => (
                    <TableRow key={result.openAlexId}>
                      <TableCell>{result.openAlexId}</TableCell>
                      <TableCell>{result.name}</TableCell>
                      <TableCell>{result.institution}</TableCell>
                      <TableCell>{result.institutionCountry}</TableCell>
                      <TableCell>
                        {isAdded(result.openAlexId) ? (
                          <IconButton color="success" disabled>
                            <CheckCircleIcon />
                          </IconButton>
                        ) : (
                          <LoadingButton
                            variant="outlined"
                            color="primary"
                            onClick={() => handleAdd(result.openAlexId)}
                            startIcon={<AddIcon />}
                            disabled={isAddingResearcherLoading}
                            loading={isAddingResearcherLoading}
                          >
                            {intl.formatMessage({
                              id: 'generic.add',
                            })}
                          </LoadingButton>
                        )}
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={5} align="center">
                      {intl.formatMessage({
                        id: 'info.noValueFound',
                      })}
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        </Paper>
      )}

      <StyledDataGrid
        loading={isLoading}
        pageSize={pageSize}
        onPageSizeChange={(newPageSize) => setPageSize(newPageSize)}
        rowsPerPageOptions={[10, 20, 30]}
        pagination
        columns={columns}
        rows={gridData}
      />
      <ConfirmationDialog
        title={intl.formatMessage({ id: 'generic.areYouSure' })}
        content={intl.formatMessage({
          id: 'researcher.removeFromListWarnContent',
        })}
        open={dialogRemoveResearcherOpen}
        onClose={handleDialogClose}
        onConfirm={handleConfirm}
        loading={isRemovingResearcherLoading}
      />
    </Box>
  );
};

export default ResearcherListDetail;
