import { useParams } from "react-router-dom"
import { useEffect, useState } from "react";
import { Group, Title, Button, Text, Grid } from "@mantine/core";
import { ActionIcon } from "@mantine/core";
import { IconHeart } from "@tabler/icons-react";
import classes from '../Card/BadgeCard.module.css';
import { Link } from "react-router-dom";
import { notifications } from '@mantine/notifications';
import MediaProfile from "../Textures/MediaProfile";
import axios from "axios";
import { IconDownload, IconPencil, IconDeviceFloppy, IconKeyframes, IconAlarm, IconFileInfo, IconStar } from "@tabler/icons-react";
import { Badge } from '@mantine/core';

export default function MediaPage(){
    let { id } = useParams();
    const [texture, setTexture] = useState(null)

    const getTextureInfo = async () => {
        const response = await axios.get('http://localhost:8080/api/videos/'+id);
        console.log(response.data)
        setTexture(response.data)
    }
    
    useEffect(() => {
        getTextureInfo()
        // eslint-disable-next-line
    }, [])

    return (
        <>
        {
            texture != null ?
            <>
                <Group justify="center" style={{backgroundColor: "black", padding: "0"}} grow>
                    <img
                        style={{
                            width: "100%",
                            maxWidth: 500
                        }}
                        alt="texture" src={"http://localhost:8080/api/videos/previews/" + texture.previewPath}/>
                </Group>
                <Group mt="xs" justify="left">
                    <Link to={"http://localhost:8080/api/videos/stream/"+texture.path} target="_blank">
                        <Button radius="md" rightSection={<IconDownload size={14} />} style={{width: 300}}>
                            Download
                        </Button>
                    </Link>
                    <Link to={"/editor"} target="_blank">
                        <Button radius="md" rightSection={<IconPencil size={14} />} variant="default" style={{width: 300}}>
                            Process
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
                <br/>
                <Group>
                    {
                        texture.tags.map((cat) => (
                            <Badge variant="light">{cat.emoji} {cat.name}</Badge>
                        ))
                    }
                </Group>
                <br/>
                {/* <Title order={2}>{texture.name}</Title> */}
                <MediaProfile texture={texture}/>
                <Group style={{marginTop: 10, marginBottom: 25}}>
                    <IconStar size={12}/>
                    <Text size={"sm"}>Favourites: 96</Text>
                </Group>
                <Grid>
                    <Grid.Col span={{xs: 12, md: 8}}>
                        <Group grow >
                            <div>
                                <Title order={3}>Description:</Title>
                                <br/>
                                <Text size="sm">
                                    {texture.description}
                                </Text>
                            </div>
                        </Group>
                    </Grid.Col>
                    <Grid.Col span={{xs: 12, md: 4}}>
                        <Title order={3}>Attributes:</Title>
                        <br/>
                        <Group>
                            <IconDeviceFloppy size={20}/> 
                            <Text size="sm">Size: {texture.size}</Text>
                        </Group>
                        <Group>
                            <IconKeyframes size={20}/>
                            <Text size="sm">FPS: {texture.fps}</Text>
                        </Group>
                        <Group>
                            <IconAlarm size={20}/>
                            <Text size="sm">Duration: {texture.duration} seconds</Text>
                        </Group>
                        <Group>
                            <IconFileInfo size={20}/>
                            <Text size="sm">Format: {texture.format}</Text>
                        </Group>
                    </Grid.Col>
                </Grid>
                <br/>
            </>
            :
            <>Loading</>
        }
        </>
    )
}