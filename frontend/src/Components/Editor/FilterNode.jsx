import { Text, Slider } from '@mantine/core';
import React, { memo } from 'react';
import { Handle, Position } from 'reactflow';
import { useState } from 'react';

export default memo(({ data, isConnectable }) => {
  const [val, setVal] = React.useState(20)

  return (
    <>
      <div style={{width: 200, background:"white", border: "1px solid black", borderRadius: 5, color:"black"}}>
        {/* {data.name} */}
        <div className='draggable'>
            <div className='draggable' style={{textAlign: "center"}}>Filter</div>
            <img className='draggable' src={data.image} style={{width: "100%", margin: "10px auto"}} alt='mask preview'/>
        </div>
        <div style={{width: "100%", padding: "0 10px 10px 10px", textAlign: "center"}}>
            <Text className='draggable' size="sm">Strength</Text>
            <Slider defaultValue={80} style={{zIndex: 10000}} showLabelOnHover/>
        </div>
      </div>
      <Handle
        type="target"
        position={Position.Left}
        id="a"
        onConnect={console.log}
        style={{padding: 4, top: 40}}
        isConnectable={isConnectable}
      />
      <Handle
        type="target"
        position={Position.Left}
        id="b"
        onConnect={console.log}
        style={{padding: 4, bottom: 40, top: "auto"}}
        isConnectable={isConnectable}
      />
    </>
  );
});