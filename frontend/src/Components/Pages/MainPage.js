import TextureCard from '../Card/TextureCard'
import { ScrollArea } from '@mantine/core'
import { Grid } from '@mantine/core'
import { NavbarNested } from '../Navbar/Navbar'
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
    
    return (
        <Grid>
            <Grid.Col span={"content"}>
                <NavbarNested/>
            </Grid.Col>
            <Grid.Col span={"auto"}>
                <div style={{overflow: "auto",height: "99vh", width:"99%"}}>
                    <ScrollArea style={{flex: 1}} scrollbars={"y"}>
                        <h2>DYNTEX))</h2>
                        <Grid style={{overflow: "hidden"}}>
                        {
                            textures.map((texture) => (
                                <Grid.Col key={texture.title} span={{xs: 12, md: 6, lg: 4}}>
                                    <TextureCard texture = {texture}/>
                                </Grid.Col>
                            ))
                        }
                        </Grid>
                    </ScrollArea>
                </div>
            </Grid.Col>
        </Grid>
    )
}