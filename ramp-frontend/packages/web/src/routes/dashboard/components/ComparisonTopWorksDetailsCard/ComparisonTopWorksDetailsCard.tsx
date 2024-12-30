import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import {
  Card,
  CardContent,
  CardHeader,
  Collapse,
  Grid,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from '@mui/material';
import React, { useState } from 'react';
import { useIntl } from 'react-intl';
import { ComparisonDetail } from 'types/comparisonTypes';
import { ResearcherStats } from 'types/researcherTypes';

const ComparisonTopWorksDetailsCard = ({
  id,
  name,
  list1Analysis,
  list2Analysis,
}: ComparisonDetail) => {
  const intl = useIntl();

  const [expanded, setExpanded] = useState(false);

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };

  const renderResearchers = (researcherStats: ResearcherStats[]) => {
    return (
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>
                {intl.formatMessage({
                  id: 'researcher.name',
                })}
              </TableCell>
              <TableCell>{intl.formatMessage({ id: 'generic.institution' })}</TableCell>
              <TableCell>{intl.formatMessage({ id: 'comparison.dataDate' })}</TableCell>
              <TableCell>{intl.formatMessage({ id: 'generic.topWorks' })}</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {researcherStats.map((stat) => {
              return (
                <React.Fragment key={stat.researcher.id}>
                  {/* Main Row */}
                  <TableRow>
                    <TableCell>{stat.researcher.name}</TableCell>
                    <TableCell>{stat.researcher.institution}</TableCell>
                    <TableCell>
                      {intl.formatDate(new Date(stat.dataDate), {
                        year: 'numeric',
                        month: '2-digit',
                        day: 'numeric',
                        hour: 'numeric',
                        minute: '2-digit',
                      })}
                    </TableCell>
                    <TableCell>{stat.workCountInTopJournals}</TableCell>
                  </TableRow>
                </React.Fragment>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>
    );
  };

  return (
    <Card style={{ marginTop: '16px' }}>
      <CardHeader
        title={name}
        subheader={intl.formatMessage({ id: 'generic.topWorks' })}
        action={
          <IconButton onClick={handleExpandClick}>
            <ExpandMoreIcon />
          </IconButton>
        }
      />
      <CardContent>
        <Grid container spacing={3}>
          <Grid item xs={6}>
            <Typography variant="h6">{list1Analysis.researcherListName}</Typography>
            <Typography variant="body1">
              {intl.formatMessage({ id: 'generic.totalTopWorks' })}
              {': '}
              <strong>
                {list1Analysis.researcherStats.reduce(
                  (acc, val) => acc + val.workCountInTopJournals,
                  0
                )}
              </strong>
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography variant="h6">{list2Analysis.researcherListName}</Typography>
            <Typography variant="body1">
              {intl.formatMessage({ id: 'generic.totalTopWorks' })}
              {': '}
              <strong>
                {list2Analysis.researcherStats.reduce(
                  (acc, val) => acc + val.workCountInTopJournals,
                  0
                )}
              </strong>
            </Typography>
          </Grid>
        </Grid>
      </CardContent>
      <Collapse in={expanded} timeout="auto" unmountOnExit>
        <CardContent>
          <Grid container spacing={3}>
            <Grid item xs={6}>
              {renderResearchers(list1Analysis.researcherStats)}
            </Grid>

            <Grid item xs={6}>
              {renderResearchers(list2Analysis.researcherStats)}
            </Grid>
          </Grid>
        </CardContent>
      </Collapse>
    </Card>
  );
};

export default ComparisonTopWorksDetailsCard;
