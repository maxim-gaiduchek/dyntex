import { useState, useEffect } from "react"
import axios from "axios"
import { Card, Grid, TextInput } from "@mantine/core"
import { UnstyledButton, Text } from "@mantine/core"
import classes from "./Tags.module.css"
import { SimpleGrid, Button } from "@mantine/core"
import { useDisclosure } from "@mantine/hooks"
import { Modal } from "@mantine/core"
import EmojiPicker from 'emoji-picker-react';
import { useComputedColorScheme } from "@mantine/core"
import { HoverCard } from "@mantine/core"
import { IconMoodEmpty } from "@tabler/icons-react"
import BaseUrl from "../../BaseUrl"
import { notifications } from '@mantine/notifications';
import { useCookies } from "react-cookie"

export default function Tags() {
    const [tags, setTags] = useState([])
    const [opened, { open, close }] = useDisclosure(false);
    const [emoji, setEmoji] = useState("")
    const [name, setName] = useState("")
    const [cookies, setCookie, removeCookie] = useCookies(['dyntex']);
    const options = {
        headers: {
        'Authorization': cookies.token
        }
    };
    const computedColorScheme = useComputedColorScheme('light', { getInitialValueInEffect: true });

    const fetchData = async () => {
        try{
            const response = await axios.get(BaseUrl+"/api/tags?pageSize=200");
            
            setTags(response.data.tags)
        } catch(e){}
    }

    useEffect(() => {
        fetchData()
    },[])

    return (
        <>
            <Modal opened={opened} onClose={close} title="Adding Tag">
                <TextInput value={name} onChange={(e) => setName(e.target.value)} placeholder="Name"/>
                <br/>
                <HoverCard width={280} openDelay={0}>
                    <HoverCard.Target>
                        <TextInput value={emoji} placeholder="Emoji (hover for keyboard)"/>
                    </HoverCard.Target>
                    <HoverCard.Dropdown className={classes.mantineHoverCardDropdown} style={{background: "none !important"}}>
                        <EmojiPicker
                            defaultCaption=""
                            theme={computedColorScheme}
                            height={400}
                            onEmojiClick={
                                (e) => {
                                    setEmoji(e.emoji)
                                }
                            }
                        />
                    </HoverCard.Dropdown>
                </HoverCard>
                <br/>
                <Button onClick={async () => {
                    try{
                        await axios.post(BaseUrl+"/api/tags", {emoji: emoji, name: name}, options)
                        fetchData()
                        close()
                        notifications.show({title: "Tag Added", message: "Tag has been added successfully", color: "teal"})
                    } catch(e){
                        notifications.show({title: "Error", message: "An error occured while adding tag", color: "red"})
                    }
                }} disabled={emoji === "" || name === ""}>Add Tag</Button>
            </Modal>
            <h2>Tags</h2>
            <Button onClick={open}>Add Tag</Button>
            <Card withBorder radius="md" className={classes.card}>
                <Grid>
                    {
                        tags.map((tag) => (
                            <Grid.Col key={tag.id} span={{xs: "4", md: "2"}}>
                                <UnstyledButton cols={{}} mt="md" className={classes.item}>
                                    <Text style={{fontSize: 30}}>
                                        {tag.emoji}
                                    </Text>
                                    <Text size="md" mt={7}>
                                        {tag.name}
                                    </Text>
                                    <Text size="xs" c={"dimmed"}>
                                        5 Medias
                                    </Text>
                                </UnstyledButton>
                            </Grid.Col>
                        ))
                    }
                </Grid>
                <SimpleGrid>
                </SimpleGrid>
            </Card>
        </>
    )
}