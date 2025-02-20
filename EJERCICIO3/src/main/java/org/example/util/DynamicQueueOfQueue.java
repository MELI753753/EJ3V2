package org.example.util;

import org.example.model.DynamicQueue;
import org.example.model.Queue;
import org.example.model.QueueOfQueue;

public class DynamicQueueOfQueue implements QueueOfQueue {

    private NodeQQ first;


    private static class NodeQQ {
        private Queue value;
        private NodeQQ next;

        public NodeQQ(Queue value, NodeQQ next) {
            this.value = value;
            this.next = next;
        }

        public Queue getValue() {
            return value;
        }

        public NodeQQ getNext() {
            return next;
        }

        public void setNext(NodeQQ next) {
            this.next = next;
        }
    }


    @Override
    public boolean isEmpty() {
        return this.first == null;
    }

    @Override
    public void add(Queue q) {
        if (this.isEmpty()) {
            this.first = new NodeQQ(q, null);
        } else {
            NodeQQ current = this.first;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new NodeQQ(q, null);
        }
    }

    @Override
    public void remove() {
        if (this.isEmpty()) {
            throw new RuntimeException("No se puede remover de una QueueOfQueue vacía.");
        }
        this.first = this.first.next;
    }

    @Override
    public Queue getFirst() {
        if (this.isEmpty()) {
            throw new RuntimeException("No se puede obtener el primero de una QueueOfQueue vacía.");
        }
        return this.first.value;
    }


    @Override
    public QueueOfQueue concatenate(QueueOfQueue... others) {
        DynamicQueueOfQueue result = new DynamicQueueOfQueue();

        NodeQQ current = this.first;
        while (current != null) {
            Queue copyQ = QueueUtil.copy(current.value);
            result.add(copyQ);
            current = current.next;
        }

        for (QueueOfQueue qoq : others) {
            if (qoq instanceof DynamicQueueOfQueue) {
                DynamicQueueOfQueue dyn = (DynamicQueueOfQueue) qoq;
                NodeQQ otherCurrent = dyn.first;
                while (otherCurrent != null) {
                    Queue copyQ = QueueUtil.copy(otherCurrent.value);
                    result.add(copyQ);
                    otherCurrent = otherCurrent.next;
                }
            } else {
                throw new RuntimeException("QueueOfQueue no soportada en concatenate");
            }
        }
        return result;
    }


    @Override
    public Queue flat() {
        Queue result = new DynamicQueue();

        NodeQQ current = this.first;
        while (current != null) {
            Queue copyQ = QueueUtil.copy(current.value);
            while (!copyQ.isEmpty()) {
                result.add(copyQ.getFirst());
                copyQ.remove();
            }
            current = current.next;
        }
        return result;
    }


    @Override
    public void reverseWithDepth() {
        reverseWithDepthRecursive();
    }

    private void reverseWithDepthRecursive() {
        if (this.isEmpty()) {
            return;
        }
        Queue front = this.getFirst();
        this.remove();
        reverseWithDepthRecursive();
        reverseQueue(front);
        this.add(front);
    }


    private void reverseQueue(Queue q) {
        if (q.isEmpty()) {
            return;
        }
        int first = q.getFirst();
        q.remove();
        reverseQueue(q);
        q.add(first);
    }
}
