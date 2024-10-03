import { useParams } from "react-router-dom"
import { useEffect, useState } from "react";
import { Group, Title, Button, Text, Grid } from "@mantine/core";
import { ActionIcon } from "@mantine/core";
import { IconHeart, IconHeartFilled, IconShare } from "@tabler/icons-react";
import classes from '../Card/BadgeCard.module.css';
import { Link } from "react-router-dom";
import { notifications } from '@mantine/notifications';
import MediaProfile from "../Textures/MediaProfile";
import axios from "axios";
import { IconDownload, IconPencil, IconDeviceFloppy, IconKeyframes, IconAlarm, IconFileInfo, IconStar } from "@tabler/icons-react";
import { Badge } from '@mantine/core';
import { useCookies } from 'react-cookie';
import { useNavigate } from 'react-router-dom';
import { getUser } from "../../utils";
import BaseUrl from "../../BaseUrl";
import PythonUrl from "../../PythonUrl";
import { ShareButton } from "../UI/ShareButton";

export default function MediaPage(props){
    let { id } = useParams();
    const [texture, setTexture] = useState(null)
    const [liked, setLiked] = useState(false)
    const [cookies, setCookie, removeCookie] = useCookies(['dyntex']);

    const options = {
        headers: {
          'Authorization': cookies.token
        }
      };

    const navigate = useNavigate()

    const getTextureInfo = async () => {
        let response;
        if(props.type === "video"){
            response = await axios.get(BaseUrl+'/api/videos/'+id);
        }else {
            response = await axios.get(BaseUrl+'/api/masks/'+id);
        }
        console.log(response.data)
        setTexture(response.data)
        checkLike(response.data)
    }
    
    const checkLike = async (t) => {
        const user = await getUser(cookies.token)
        console.log(user)
        setLiked(user === undefined ? false : user.likedMedia?.some(media => media.id === t.id) || false)
    }

    useEffect(() => {
        getTextureInfo()
        // eslint-disable-next-line
    }, [])

    const startEditor = async () =>{
        try{
            const response = await axios.get(PythonUrl+"/start?id="+texture.id)

            window.open("/editor/" + response.data.id, '_blank').focus();
        }catch(e){}
    }

    const likeVideo = async () => {
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

    return (
        <>
        {
            texture != null ?
            <>
                <Group justify="center" style={{backgroundColor: props.type==="video" ? "black" : "white", padding: "0"}} grow>
                    {
                        props.type === "mask" ?
                        <img
                        style={{
                            width: "100%",
                            maxWidth: 500
                        }}
                        alt="texture" src={BaseUrl+"/api/media/previews/" + texture.path}/>
                        :
                        <video 
                            className={classes.video}
                            loop
                            muted
                            autoPlay 
                            style={{
                                width: "100%",
                                maxWidth: 600
                            }}>
                            <source src={BaseUrl+"/api/media/stream/" + texture.path} type="video/mp4" />
                            Your browser does not support the video tag.
                        </video>
                    }
                </Group>
                <Group mt="xs" justify="left">
                    <Link to={BaseUrl+"/api/"+(props.type)+"s/download/"+texture.path} target="_blank">
                        <Button radius="md" rightSection={<IconDownload size={14} />} style={{width: 300}}>
                            Download
                        </Button>
                    </Link>
                    {
                        props.type === "video" && 
                        <Button radius="md" onClick={startEditor} rightSection={<IconPencil size={14} />} variant="default" style={{width: 300}}>
                            Process
                        </Button>
                    }
                    {
                        props.type === "video" && 
                        <ActionIcon
                        onClick={likeVideo}
                        variant="default" radius="md" size={36}>
                            {
                                liked ? <IconHeartFilled size={24} color="red"/> : <IconHeart size={24}/>
                            }
                        </ActionIcon>
                    }
                    <ShareButton url={window.location.href}/>
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
                    <Text size={"sm"}>Favourites: {texture.likes}</Text>
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
                        {
                            props.type === "video" &&
                            <>
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
                            </>
                        }
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