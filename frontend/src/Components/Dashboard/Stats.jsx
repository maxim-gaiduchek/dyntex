import { Group, Paper, SimpleGrid, Text } from '@mantine/core';
import {
  IconUserPlus,
  IconCamera,
  IconBookmark,
  IconPhoto,
} from '@tabler/icons-react';
import classes from './StatsGrid.module.css';


const data = [
  { title: 'Textures', icon: <IconCamera/>, value: '13,456', diff: 34 },
  { title: 'Masks', icon: <IconPhoto/>, value: '4,145', diff: -13 },
  { title: 'Tags', icon: <IconBookmark/>, value: '745', diff: 18 },
  { title: 'New Users', icon: <IconUserPlus/>, value: '188', diff: -30 },
];

export default function Stats() {
  const stats = data.map((stat) => {

    return (
      <Paper withBorder p="md" radius="md" key={stat.title}>
        <Group justify="space-between">
          <Text size="xs" c="dimmed" className={classes.title}>
            {stat.title}
          </Text>
          {stat.icon}
        </Group>

        <Group align="flex-end" gap="xs" mt={25}>
          <Text className={classes.value}>{stat.value}</Text>
        </Group>

        <Text fz="xs" c="dimmed" mt={7}>
          Total records in database
        </Text>
      </Paper>
    );
  });
  return (
    <div className={classes.root}>
      <SimpleGrid cols={{ base: 1, xs: 2, md: 4 }}>{stats}</SimpleGrid>
    </div>
  );
}