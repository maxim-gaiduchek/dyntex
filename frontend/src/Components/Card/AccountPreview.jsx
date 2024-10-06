import { Avatar, Text, Button, Paper } from '@mantine/core';
import { Link } from 'react-router-dom';

export default function AccountPreview(props) {
  return (
    <Paper radius="md" withBorder p="lg" bg="var(--mantine-color-body)">
      <Avatar
        src="https://as2.ftcdn.net/v2/jpg/02/08/98/05/1000_F_208980504_njS12KTuZLQ3wQZaHLbKpSLFNu9rF6Hs.jpg"
        size={120}
        radius={120}
        mx="auto"
      />
      <Text ta="center" fz="lg" fw={500} mt="md">
        {props.account.name}
      </Text>
      <Text ta="center" c="dimmed" fz="sm">
        {props.account.email}
      </Text>

      <Link to="/account/kvoza">
        <Button variant="default" fullWidth mt="md">
            View Account
        </Button>
      </Link>
    </Paper>
  );
}