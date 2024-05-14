import React, { memo } from 'react';
import { Handle, Position } from 'reactflow';

export default memo(({ data, isConnectable }) => {
  return (
    <>
      <div>
        {data.name}
      </div>
      <img src={data.image} style={{width: "200px", margin: "0 auto"}} alt='video preview'/>
      <Handle
        type="source"
        position={Position.Right}
        id="b"
        style={{
          color: "Output",
          padding: 4
        }}
        isConnectable={isConnectable}
      >
      </Handle>
    </>
  );
});