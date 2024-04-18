import { useParams } from "react-router-dom"
import { useEffect, useState } from "react";
import { Group, Title, Button } from "@mantine/core";
import { ActionIcon } from "@mantine/core";
import { IconHeart } from "@tabler/icons-react";
import classes from '../Card/BadgeCard.module.css';
import { Link } from "react-router-dom";
import { notifications } from '@mantine/notifications';
import MediaProfile from "../Textures/MediaProfile";
import axios from "axios";

export default function MediaPage(){
    let { id } = useParams();
    const [texture, setTexture] = useState(null)

    const getTexture = async () => {
        const response = await axios.get('http://localhost:8080/api/videos/'+id);
        console.log(response.data)
        setTexture(response.data)
    }
    
    useEffect(() => {
        getTexture()
    }, [])

    return (
        <>
        {
            texture != null ?
            <>
                <Group justify="center" grow>
                    <img
                        style={{
                            width: "100%",
                            maxWidth: 500
                        }}
                        alt="texture" src={"http://localhost:8080/api/videos/previews/" + texture.previewPath}/>
                </Group>
                <Group mt="xs">
                    <Link to={"/media/" + texture.id} style={{ flex: 1 }}>
                    <Button radius="md" style={{width: 500}}>
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
                <br/>
                {/* <Title order={2}>{texture.name}</Title> */}
                <MediaProfile texture={texture}/>
            </>
            :
            <>Loading</>
        }
        </>
    )
}