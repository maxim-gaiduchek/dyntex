import { IconHeart, IconHeartFilled } from '@tabler/icons-react';
import { Card, Image, Text, Group, Badge, Button, ActionIcon, HoverCard } from '@mantine/core';
import classes from './BadgeCard.module.css';
import { notifications } from '@mantine/notifications';
import { Link } from 'react-router-dom';
import AccountPreview from './AccountPreview';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import { useState } from 'react';
import BaseUrl from '../../BaseUrl';

export default function TextureCard(props) {
  var { id, name, tags, description, previewPath, size, fps, createdBy } = props.texture;
  
  const [cookies, setCookie, removeCookie] = useCookies(['dyntex']);
  const [liked, setLiked] = useState(props.liked)
  previewPath = props.texture.previewPath || props.texture.path.replace(/\.[^/.]+$/, "") + ".png";
  const options = {
    headers: {
      'Authorization': cookies.token
    }
  };
  const features = tags.map((badge) => (
    <Badge variant="light" key={badge.emoji} leftSection={badge.emoji}>
      {badge.name}
    </Badge>
  ));

  return (
    <Card withBorder radius="md" p="md" className={classes.card}>
      <Card.Section>
        <Link to={"/media/"+id}>
          <Image src={BaseUrl+"/api/media/previews/"+previewPath} alt={name} height={180} />
        </Link>
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
          {
            createdBy !== null &&
            <HoverCard.Target>
              <span className={classes.accountLink}>Added by: <Link to={"/account/" + createdBy.id}>{createdBy.name}</Link></span>
            </HoverCard.Target>
          }
          <HoverCard.Dropdown>
            <AccountPreview account={createdBy}/>
          </HoverCard.Dropdown>
        </HoverCard>
        <Text fz="sm" mt="xs">
          {description.slice(0, 30)}{description.length > 30 ? "..." : ""}
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
        onClick={async () => 
          {
            const response = await axios.put(BaseUrl+"/api/videos/"+id+"/likes/"+cookies.id, {}, options)

            if(liked === false){
              notifications.show({
                title: 'Texture added',
                message: 'Texture has been added to favourites! ⭐',
                color: "green"
              })
            }else{
              notifications.show({
                title: 'Texture removed',
                message: 'Texture has been removed from favourites! ⭐',
                color: "green"
              })
            }

            setLiked(!liked)
          }
        }
        variant="default" radius="md" size={36}>
          {
            liked ? <IconHeartFilled size={24} color="red"/> : <IconHeart size={24}/>
          }
        </ActionIcon>
      </Group>
    </Card>
  );
}