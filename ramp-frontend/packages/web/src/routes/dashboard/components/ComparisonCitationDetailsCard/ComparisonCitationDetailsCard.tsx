import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import {
  Box,
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

const ComparisonCitationDetailsCard = ({
  id,
  name,
  list1Analysis,
  list2Analysis,
}: ComparisonDetail) => {
  const intl = useIntl();

  const [expanded, setExpanded] = useState(false);
  const [expandedRows, setExpandedRows] = useState<{ [key: string]: boolean }>({});

  const toggleRow = (id: string) => {
    setExpandedRows((prev) => ({
      ...prev,
      [id]: !prev[id],
    }));
  };

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
              <TableCell>{intl.formatMessage({ id: 'genetic.totalCitations' })}</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {researcherStats.map((stat) => {
              const totalCitations = Object.values(stat.citationCountPerYear).reduce(
                (acc, val) => acc + val,
                0
              );

              const isExpanded = expandedRows[stat.researcher.id] || false;

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
                    <TableCell>{totalCitations}</TableCell>
                    <TableCell>
                      <IconButton
                        onClick={() => toggleRow(stat.researcher.id.toString())}
                        size="small"
                      >
                        <ExpandMoreIcon
                          sx={{
                            transform: isExpanded ? 'rotate(180deg)' : 'rotate(0)',
                            transition: 'transform 0.2s',
                          }}
                        />
                      </IconButton>
                    </TableCell>
                  </TableRow>

                  {/* Expandable Row */}
                  <TableRow>
                    <TableCell colSpan={5} style={{ paddingBottom: 0, paddingTop: 0 }}>
                      <Collapse in={isExpanded} timeout="auto" unmountOnExit>
                        <Box margin={1}>
                          <Table size="small">
                            <TableHead>
                              <TableRow>
                                <TableCell align="center">
                                  {intl.formatMessage({ id: 'generic.year' })}
                                </TableCell>
                                <TableCell align="center">
                                  {intl.formatMessage({ id: 'generic.citations' })}
                                </TableCell>
                              </TableRow>
                            </TableHead>
                            <TableBody>
                              {Object.entries(stat.citationCountPerYear).map(
                                ([year, citations]) => (
                                  <TableRow key={year}>
                                    <TableCell align="center">{year}</TableCell>
                                    <TableCell align="center">{citations}</TableCell>
                                  </TableRow>
                                )
                              )}
                            </TableBody>
                          </Table>
                        </Box>
                      </Collapse>
                    </TableCell>
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
        subheader={intl.formatMessage({ id: 'generic.citations' })}
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
              {intl.formatMessage({ id: 'generic.mean' })}
              {': '}
              <strong>{list1Analysis.mean.toFixed(2)}</strong>
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography variant="h6">{list2Analysis.researcherListName}</Typography>
            <Typography variant="body1">
              {intl.formatMessage({ id: 'generic.mean' })}
              {': '}
              <strong>{list2Analysis.mean.toFixed(2)}</strong>
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

export default ComparisonCitationDetailsCard;
