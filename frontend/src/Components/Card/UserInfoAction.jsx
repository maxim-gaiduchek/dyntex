import { Avatar, Text, Button, Paper } from '@mantine/core';
import { Link } from 'react-router-dom';

export function UserInfoAction(props) {
  console.log(props.user)
  return (
    <Paper radius="md" withBorder p="lg" bg="var(--mantine-color-body)">
      <Avatar
        src="/avatar.png"
        size={120}
        radius={120}
        mx="auto"
      />
      <Text ta="center" fz="lg" fw={500} mt="md">
        {props.user.name}
      </Text>
      <Text ta="center" c="dimmed" fz="sm">
        {props.user.email} • {props.user.createdMasks.length + props.user.createdVideos.length} Medias created
      </Text>

      <Link to={"/account/"+props.user.id}>
        <Button variant="default" fullWidth mt="md">
            View Profile
        </Button>
      </Link>
    </Paper>
  );
}