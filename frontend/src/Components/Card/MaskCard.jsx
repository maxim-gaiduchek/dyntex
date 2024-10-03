import { IconBookmark, IconHeart, IconShare } from '@tabler/icons-react';
import {
  Card,
  Image,
  Text,
  ActionIcon,
  Badge,
  Group,
  Center,
  Avatar,
  useMantineTheme,
  rem,
} from '@mantine/core';
import classes from './MaskCard.module.css';
import BaseUrl from '../../BaseUrl';
import { Link } from 'react-router-dom';
import { ShareButton } from '../UI/ShareButton';

export default function MaskCard(props) {
  const linkProps = { href: '/mask/' + props.mask.id, target: '_self', rel: 'Mask' };
  const theme = useMantineTheme();

  console.log(props.mask)

  return (
    <Card withBorder radius="md" className={classes.card}>
      <Card.Section>
        <a {...linkProps}>
          <Image src={BaseUrl + "/api/media/previews/"+props.mask.path} height={180} />
        </a>
      </Card.Section>

      {/* <Badge className={classes.rating} variant="gradient" gradient={{ from: 'yellow', to: 'red' }}>
        outstanding
      </Badge> */}

      <Text className={classes.title} fw={500} component="a" {...linkProps}>
        {props.mask.name}
      </Text>

      <Text fz="sm" c="dimmed" lineClamp={4}>
        {props.mask.description}
      </Text>

      <Group justify="space-between" className={classes.footer}>
        <Link to={"/account/"+props.mask.createdBy.id}>
            <Center>
            <Avatar
                src={props.mask.createdBy.avatar !== null ? BaseUrl + "/api/media/avatars/" + props.mask.createdBy.avatar : ""}
                size={24}
                radius="xl"
                mr="xs"
            />
            <Text fz="sm" inline>
                {props.mask.createdBy.name}
            </Text>
            </Center>
        </Link>

        <Group gap={8} mr={0}>
          {/* <ActionIcon className={classes.action}>
            <IconHeart style={{ width: rem(16), height: rem(16) }} color={theme.colors.red[6]} />
          </ActionIcon> */}
          {/* <ActionIcon className={classes.action}>
            <IconBookmark
              style={{ width: rem(16), height: rem(16) }}
              color={theme.colors.yellow[7]}
            />
          </ActionIcon> */}
          <ShareButton url={window.location.origin + "/mask/" + props.mask.id} />
        </Group>
      </Group>
    </Card>
  );
}