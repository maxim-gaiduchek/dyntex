import { ScrollArea } from '@mantine/core'
import { Grid } from '@mantine/core'
import { NavbarNested } from '../Navbar/Navbar'

export default function MainPage(){
    
    return (
        <Grid>
            <Grid.Col span={"content"}>
                <NavbarNested/>
            </Grid.Col>
            <Grid.Col span={"auto"}>
                <div style={{overflow: "auto",height: "99vh", width:"99%"}}>
                    <ScrollArea style={{flex: 1}} scrollbars={"y"}>
                        <h2>SECOND PAGE</h2>
                    </ScrollArea>
                </div>
            </Grid.Col>
        </Grid>
    )
}