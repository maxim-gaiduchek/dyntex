import { Button } from '@mantine/core';
import React, { memo } from 'react';
import { Handle, Position } from 'reactflow';
import axios from 'axios';

export default memo(({ data, isConnectable }) => {
  return (
    <>
      <div style={{width: 200, minHeight: 200, background:"white", border: "1px solid black", borderRadius: 5, color:"black"}}>
        {/* {data.name} */}
        <div style={{textAlign: "center"}}>Output</div>
        <img src={data.image} style={{width: "100%", margin: "10px auto"}} alt='mask preview'/>
        <div style={{width: "100%", paddingBottom: 10, textAlign: "center"}}>
            <Button onClick={async (e) => {
              e.preventDefault();
              const link = "http://localhost:5000/save"
              const d = await data.handleDownload(e)

              try{
                const resp = await axios.post(link, { data: d, session_id: data.session_id })
                window.open("http://localhost:5000/download/" + resp.data.link, '_blank').focus();
              }catch(err){
                alert("Unknown error occured =(")
              }
              
            }}>Download</Button>
        </div>
      </div>
      <Handle
        type="target"
        position={Position.Left}
        id="b"
        onConnect={console.log}
        style={{padding: 4}}
        isConnectable={isConnectable}
      />
    </>
  );
});