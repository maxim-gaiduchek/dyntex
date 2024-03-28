import TextureCard from '../Card/TextureCard'
import { ScrollArea } from '@mantine/core'
import { Grid } from '@mantine/core'
import { NavbarNested } from '../Navbar/Navbar'
import { Pagination } from '@mantine/core';
import { Center, Group } from '@mantine/core';
import { SegmentedControl } from '@mantine/core';
import React from 'react';

import CategorySearch from '../Textures/CategorySearch';
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
      title: "weed",
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
    
    return (
        <Grid>
            <Grid.Col span={"content"}>
                <NavbarNested/>
            </Grid.Col>
            <Grid.Col span={"auto"}>
                <div style={{overflow: "auto",height: "99vh", width:"99%"}}>
                  <ScrollArea style={{flex: 1}} scrollbars={"y"}>
                      <h2>DYNTEX))</h2>
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
                  </ScrollArea>
                </div>
            </Grid.Col>
        </Grid>
    )
}