import { Grid } from '@mantine/core'
import { Title, Text } from '@mantine/core'
import TextureCard from '../Card/TextureCard'
import { Button } from '@mantine/core';

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

export default function Account(){
    
    return (
        <>
          <Grid>
              <Grid.Col span={3}>
                  <div className='avatar' style={{
                      aspectRatio: "1/1",
                      width: "100%", 
                      backgroundColor: "red",
                      backgroundImage: "url(https://avatars.githubusercontent.com/u/58140020?v=4)",
                      backgroundSize: "cover",
                      backgroundPosition: "center",
                      marginBottom: 20
                  }}>
                  </div>
                  <Title order={3}>Kvoza Onkay</Title>
                  <Text c="dimmed" size="xs">stojkiva@fit.cvut.cz</Text>
                  Role: Admin <br/>
                  Textures: 5
                  <br/>
              </Grid.Col>
              <Grid.Col span={9}>
                  <Title order={3}>Kvoza Onkay</Title>
                  <Text c="dimmed" size="xs">stojkiva@fit.cvut.cz</Text>
                  Role: Admin <br/>
                  Textures: 5
                  <br/>
              </Grid.Col>
          </Grid>
          <br/>
          <Title order={3}>Uploaded Textures:</Title>
          <Grid className='textureGrid'>
              {
                  textures.map((texture) => (
                      <Grid.Col key={texture.title} span={{xs: 12, md: 6, lg: 4}}>
                          <TextureCard texture = {texture}/>
                      </Grid.Col>
                  ))
              }
          </Grid>
        </>
    )
}