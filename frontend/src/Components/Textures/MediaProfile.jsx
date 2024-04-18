import { Avatar, Text, Group } from '@mantine/core';
import { IconPhoneCall, IconAt } from '@tabler/icons-react';
import classes from './MediaProfile.module.css';

export default function MediaProfile(props) {
  return (
    <div>
      <Group wrap="nowrap">
        <Avatar
          src="https://avatars.githubusercontent.com/u/58140020?v=4"
          size={94}
          radius="md"
        />
        <div>
          <Text fz="xs" tt="uppercase" fw={700}>
            {props.texture.name} by
          </Text>

          <Text fz="lg" fw={500} className={classes.name}>
            Kvoza Onkay
          </Text>

          <Group wrap="nowrap" gap={10} mt={3}>
            <IconAt stroke={1.5} size="1rem" className={classes.icon} />
            <Text fz="xs" c="dimmed">
              kvoza@gmail.com
            </Text>
          </Group>
        </div>
      </Group>
    </div>
  );
}