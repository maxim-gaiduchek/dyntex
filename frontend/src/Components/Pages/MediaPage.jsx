import { useParams } from "react-router-dom"

export default function MediaPage(){
    let { id } = useParams();

    return (
        <>
        <h1>Media Page</h1>
        id: {id}
        </>
    )
}