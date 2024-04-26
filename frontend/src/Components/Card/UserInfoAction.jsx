import { Avatar, Text, Button, Paper } from '@mantine/core';
import { Link } from 'react-router-dom';

export function UserInfoAction(props) {
  return (
    <Paper radius="md" withBorder p="lg" bg="var(--mantine-color-body)">
      <Avatar
        src="https://avatars.githubusercontent.com/u/58140020?v=4"
        size={120}
        radius={120}
        mx="auto"
      />
      <Text ta="center" fz="lg" fw={500} mt="md">
        {props.user.name}
      </Text>
      <Text ta="center" c="dimmed" fz="sm">
        {props.user.email} • {props.user.createdMedia.length} Medias created
      </Text>

      <Link to={"/account/"+props.user.id}>
        <Button variant="default" fullWidth mt="md">
            View Profile
        </Button>
      </Link>
    </Paper>
  );
}