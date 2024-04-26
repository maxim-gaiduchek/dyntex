import { useState, useEffect } from "react"
import axios from "axios"
import { Card, Grid } from "@mantine/core"
import { UnstyledButton, Text } from "@mantine/core"
import classes from "./Tags.module.css"
import { SimpleGrid } from "@mantine/core"

export default function Tags() {
    const [tags, setTags] = useState([])

    const fetchData = async () => {
        try{
            const response = await axios.get("http://localhost:8080/api/tags");
            
            setTags(response.data.tags)
        } catch(e){}
    }

    useEffect(() => {
        fetchData()
    },[])

    return (
        <>
            <h2>Tags</h2>
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