import TextureCard from '../Card/TextureCard'
import { Grid } from '@mantine/core'
import { Pagination } from '@mantine/core';
import { Center, Group } from '@mantine/core';
import { SegmentedControl, Modal, Button } from '@mantine/core';
import React from 'react';
import { useDisclosure } from '@mantine/hooks';
import { useEffect } from 'react';
import { Skeleton } from '@mantine/core';
import axios from 'axios';

import CategorySearch from '../Textures/CategorySearch';
import DropZone from '../Textures/DropZone';

export default function MainPage(){

    const [value, setValue] = React.useState("All")
    const [textures, setTextures] = React.useState(null)
    const [opened, { open, close }] = useDisclosure(false);

    const fetchData = async () => {
      setTextures(null)
      const response = await axios.get('http://localhost:8080/api/videos');
      console.log(response)
      setTextures(response.data.videos)
    }

    useEffect(() => {
      fetchData()
    },[])

    return (
        <>
          <h2>DYNTEX))</h2>
          <Modal opened={opened} onClose={close} title="Add Texture" size="lg">
            <DropZone fetchData={fetchData} close={close}/>
          </Modal>
          <Group justify='right'>
            <Grid style={{paddingRight: 10}}>
              <Button onClick={open}>Add Texture</Button>
            </Grid>
          </Group>
          <br/>
          <Group justify="space-between" grow>
            <CategorySearch/>
            <SegmentedControl onChange={setValue} value={value} data={['All', 'Texture', 'Mask']} />
          </Group>
          <div style={{marginBottom: 20}}>
          </div>
          <Grid style={{overflow: "hidden"}}>
          {
            textures !== null ?
            <>
              {
                textures.length === 0 ?
                <Grid.Col span={12}>
                  <Group justify='center' grow>
                    <h2 style={{textAlign: "center"}}>No textures yet.</h2>
                  </Group>
                </Grid.Col>
                :
                <>
                  {
                      textures.map((texture) => (
                          <Grid.Col key={texture.title} span={{xs: 12, md: 6, lg: 4}}>
                              <TextureCard texture = {texture}/>
                          </Grid.Col>
                      ))
                  }
                </>
              }
            </>
            :
            <>
              <Grid.Col span={{xs: 12, md: 6, lg: 4}}>
                  <Skeleton visible={true} width={"100%"} height={250}/>
              </Grid.Col>
              <Grid.Col span={{xs: 12, md: 6, lg: 4}}>
                  <Skeleton visible={true} width={"100%"} height={250}/>
              </Grid.Col>
              <Grid.Col span={{xs: 12, md: 6, lg: 4}}>
                  <Skeleton visible={true} width={"100%"} height={250}/>
              </Grid.Col>
            </>
          }
          </Grid>
          <Center maw={"100vw"} h={100}>
            <Pagination mt="sm" total={10} />
          </Center>
        </>
    )
}