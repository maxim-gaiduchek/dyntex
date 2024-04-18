import { IconHeart } from '@tabler/icons-react';
import { Card, Image, Text, Group, Badge, Button, ActionIcon, HoverCard } from '@mantine/core';
import classes from './BadgeCard.module.css';
import { notifications } from '@mantine/notifications';
import { Link } from 'react-router-dom';
import AccountPreview from './AccountPreview';

export default function TextureCard(props) {
  const { id, path, name, tags, description, size, fps } = props.texture;
  const features = tags.map((badge) => (
    <Badge variant="light" key={badge.label} leftSection={badge.emoji}>
      {badge.label}
    </Badge>
  ));

  return (
    <Card withBorder radius="md" p="md" className={classes.card}>
      <Card.Section>
        <Image src={path} alt={name} height={180} />
      </Card.Section>

      <Card.Section className={classes.section} mt="md">
        <Group justify="apart">
          <Text fz="lg" fw={500}>
            <Link to={"/media/"+id}>{name}</Link>
          </Text>
          <Badge size="sm" variant="light">
            {/* {country} */}
            Texture
          </Badge>
        </Group>
        <HoverCard width={280} openDelay={300} shadow="md">
          <HoverCard.Target>
            <span className={classes.accountLink}>Added by: <Link to="/account/asd">Kvoza Onkay</Link></span>
          </HoverCard.Target>
          <HoverCard.Dropdown>
            <AccountPreview/>
          </HoverCard.Dropdown>
        </HoverCard>
        <Text fz="sm" mt="xs">
          {description}
        </Text>
        <Text size={"xs"} c="dimmed" fz="sm" mt="xs">
          Size: {size}
          <br/>
          Framerate: {fps} fps
        </Text>
        <Text mt="md" className={classes.label}>
          This texture is part of these categories:
        </Text>
        <Group gap={7} mt={5}>
          {features}
        </Group>
      </Card.Section>

      <Group mt="xs">
        <Link to={"/media/" + id} style={{ flex: 1 }}>
          <Button radius="md" style={{width: "100%"}}>
            Show details
          </Button>
        </Link>
        <ActionIcon
        onClick={() =>
          notifications.show({
            title: 'Texture added',
            message: 'Texture has been added to favourites! ⭐',
            color: "green"
          })
        }
        variant="default" radius="md" size={36}>
          <IconHeart className={classes.like} stroke={1.5} />
        </ActionIcon>
      </Group>
    </Card>
  );
}