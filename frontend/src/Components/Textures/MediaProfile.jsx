import { Avatar, Text, Group } from '@mantine/core';
import { IconAt } from '@tabler/icons-react';
import classes from './MediaProfile.module.css';

export default function MediaProfile(props) {
  return (
    <div>
      <Group wrap="nowrap" style={{position: "relative"}}>
        <Avatar
          src="/avatar.png"
          size={94}
          radius="md"
        />
        <div>
          <Text fz="xs" tt="uppercase" fw={700}>
            {props.texture.name} by
          </Text>

          <Text fz="lg" fw={500} className={classes.name}>
            {props.texture.createdBy.name}
          </Text>

          <Group wrap="nowrap" gap={10} mt={3}>
            <IconAt stroke={1.5} size="1rem" className={classes.icon} />
            <Text fz="xs" c="dimmed">
              {props.texture.createdBy.email}
            </Text>
          </Group>
        </div>
        <Text size="sm" c='dimmed' style={{position: "absolute", top: 0, right: 20}}>Published: Mar 28, 2024</Text>
      </Group>
    </div>
  );
}