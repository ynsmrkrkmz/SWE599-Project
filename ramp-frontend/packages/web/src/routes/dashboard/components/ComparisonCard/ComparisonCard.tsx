import { Box, Button, Card, CardContent, Typography } from '@mui/material';
import { FC } from 'react';
import { useIntl } from 'react-intl';
import { useNavigate } from 'react-router-dom';

type Props = {
  id: number;
  name: string;
};

const ComparisonCard: FC<Props> = ({ id, name }) => {
  const intl = useIntl();
  const navigate = useNavigate();

  const handleViewList = () => {
    navigate(`${id}`);
  };
  return (
    <Card
      key={id}
      variant="outlined"
      sx={{ maxWidth: 300, margin: 'auto', textAlign: 'center', padding: 2 }}
    >
      <CardContent>
        <Typography variant="h6" component="div" gutterBottom>
          {name}
        </Typography>
        <Box mt={2}>
          <Button variant="outlined" onClick={handleViewList}>
            {intl.formatMessage({
              id: 'generic.viewList',
            })}
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
};

export default ComparisonCard;
