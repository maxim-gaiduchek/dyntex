import TextureCard from '../Card/TextureCard'
import { Grid } from '@mantine/core'
import { Pagination } from '@mantine/core';
import { Center, Group } from '@mantine/core';
import { SegmentedControl, Modal, Button } from '@mantine/core';
import React from 'react';
import { useDisclosure } from '@mantine/hooks';

import CategorySearch from '../Textures/CategorySearch';
import DropZone from '../Textures/DropZone';
const textures = [
    {
      image: "https://img.freepik.com/free-photo/relief-texture-brown-bark-tree-close-up_158595-6482.jpg",
      title: "Wood =)",
      country: "new",
      description: "Cool wood texture =)))",
      badges: [
        {emoji: "☀️", label: "Nature"}
      ]
    },
    {
      image: "https://img.freepik.com/free-photo/grass-texture-background_64049-124.jpg",
      title: "Weed",
      country: "idk",
      description: "Cool grass texture =)))",
      badges: [
        {emoji: "☀️", label: "Nature"}
      ]
    },
    {
      image: "https://img.freepik.com/free-photo/relief-texture-brown-bark-tree-close-up_158595-6482.jpg",
      title: "Wood =)",
      country: "Croatia",
      description: "Cool wood texture =)))",
      badges: [
        {emoji: "☀️", label: "Nature"}
      ]
    },
    {
      image: "https://img.freepik.com/free-photo/relief-texture-brown-bark-tree-close-up_158595-6482.jpg",
      title: "Wood =)",
      country: "Croatia",
      description: "Cool wood texture =)))",
      badges: [
        {emoji: "☀️", label: "Nature"}
      ]
    },{
      image: "https://img.freepik.com/free-photo/relief-texture-brown-bark-tree-close-up_158595-6482.jpg",
      title: "Wood =)",
      country: "Croatia",
      description: "Cool wood texture =)))",
      badges: [
        {emoji: "☀️", label: "Nature"}
      ]
    }
  ]

export default function MainPage(){

    const [value, setValue] = React.useState("All")
    const [opened, { open, close }] = useDisclosure(false);

    return (
        <>
          <h2>DYNTEX))</h2>
          <Modal opened={opened} onClose={close} title="Add Texture" size="lg">
            <DropZone/>
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
              textures.map((texture) => (
                  <Grid.Col key={texture.title} span={{xs: 12, md: 6, lg: 4}}>
                      <TextureCard texture = {texture}/>
                  </Grid.Col>
              ))
          }
          </Grid>
          <Center maw={"100vw"} h={100}>
            <Pagination mt="sm" total={10} />
          </Center>
        </>
    )
}