import { Avatar, Text, Button, Paper } from '@mantine/core';
import { Link } from 'react-router-dom';

export default function AccountPreview() {
  return (
    <Paper radius="md" withBorder p="lg" bg="var(--mantine-color-body)">
      <Avatar
        src="https://avatars.githubusercontent.com/u/58140020?v=4"
        size={120}
        radius={120}
        mx="auto"
      />
      <Text ta="center" fz="lg" fw={500} mt="md">
        Kvoza Onkay
      </Text>
      <Text ta="center" c="dimmed" fz="sm">
        ivannstojka@Outlook.com
      </Text>

      <Link to="/account/kvoza">
        <Button variant="default" fullWidth mt="md">
            View Account
        </Button>
      </Link>
    </Paper>
  );
}