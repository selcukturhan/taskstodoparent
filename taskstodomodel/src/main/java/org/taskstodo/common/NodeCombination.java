package org.taskstodo.common;

public class NodeCombination {
    
    Long sourceNodeId;
    Long movedNodeId;
    Long targetNodeId;

    public NodeCombination() {
    }

    public NodeCombination(Long sourceNodeId, Long movedNodeId,
            Long targetNodeId) {
        super();
        this.sourceNodeId = sourceNodeId;
        this.movedNodeId = movedNodeId;
        this.targetNodeId = targetNodeId;
    }

    public Long getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(Long sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public Long getMovedNodeId() {
        return movedNodeId;
    }

    public void setMovedNodeId(Long movedNodeId) {
        this.movedNodeId = movedNodeId;
    }

    public Long getTargetNodeId() {
        return targetNodeId;
    }

    public void setTargetNodeId(Long targetNodeId) {
        this.targetNodeId = targetNodeId;
    }
}