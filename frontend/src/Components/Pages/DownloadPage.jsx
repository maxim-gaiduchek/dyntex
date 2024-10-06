import BaseUrl from "../../BaseUrl"
import PythonUrl from "../../PythonUrl"
import { useEffect, useState, useRef } from "react"
import { useParams } from "react-router-dom"
import { LoadingOverlay } from "@mantine/core"
import { Button } from "@mantine/core"
import { Progress } from '@mantine/core';
export default function DownloadPage() {
    let { id } = useParams();
    const [progress, setProgress] = useState(undefined)
    const intervalRef = useRef(null);

    const loadData = async () => {
        try{
            const response = await fetch(PythonUrl+"/progress?sessionid="+id)
            const data = await response.json()
            setProgress(data)
            if(data.output !== ""){
                clearInterval(intervalRef.current);
            }
        }catch(e){
            console.log(e)
        }
    }

    useEffect(() => {
        intervalRef.current = setInterval(loadData, 1000);
        return () => clearInterval(intervalRef.current);
    },[])

    return (
        <div style={{width: "100%", height: "100%"}}>
            <LoadingOverlay visible={progress === undefined}/>
            {
                progress !== undefined ?
                <div style={{width: "100%", height: "100%", display: "flex", justifyContent: "center", alignItems: "center"}}>
                    <div style={{width: "100%", height: "100%", display: "flex", justifyContent: "center", alignItems: "center", flexDirection: "column"}}>
                        {
                            progress.output !== "" ?
                            <h2>Download ready</h2>
                            :
                            <h2>Processing...</h2>
                        }
                        <Progress style={{width: "500px"}} value={(progress.output !== "" ? "100" : progress.progress.toFixed(2))} size="md"/>
                        <br/>
                        {
                            progress.output !== "" ?
                            <>
                                {progress.output}
                                <a target="_blank" href={PythonUrl + "/download/" + progress.output}>
                                    <Button style={{marginTop: 10}}>Download</Button>
                                </a>
                            </>
                            :
                            <>
                                Items: {progress.item}/{progress.items}
                            </>
                        }
                    </div>
                </div>
                :
                null
            }
        </div>
    )
}