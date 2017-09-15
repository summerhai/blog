package canghailongyin.blog.leetcode.linkedlist;

import canghailongyin.blog.basic_structure.ListNode;

/**
 * Created by mingl on 2017-9-9.
 * 终于不怕单链表翻转了！！
 */
public class ResverseLinkList {

    public ListNode resverseLinkList(ListNode head) {
        if (head == null)
            return null;
        ListNode newHead = null;
        ListNode curNode = head;
        ListNode preNode = null;
        while (curNode != null) {
            ListNode nextNode = curNode.next;
            if (nextNode == null) {
                newHead = curNode;
            }
            curNode.next = preNode;
            preNode = curNode;
            curNode = nextNode;
        }
        return newHead;
    }

    /**
     * Given 1->2->3->4->5->NULL, m = 2 and n = 4,
     * <p>
     * return 1->4->3->2->5->NULL.
     *
     * @param head
     * @param start
     * @param end
     * @return
     */
    public ListNode resverseLinkList(ListNode head, int start, int end) {
        if (head == null || head.next == null)
            return head;
        ListNode newHead = new ListNode(-1);
        newHead.next = head;
        ListNode beforeM = newHead;
        ListNode node1 = null, node2 = null;
        for (int i = 0; i < end; i++) {
            if (i < start - 1) {
                beforeM = beforeM.next;
            } else if (i == start - 1) {
                node1 = beforeM.next;
                node2 = node1.next;
            } else {
                node1.next = node2.next;
                node2.next = beforeM.next;
                beforeM.next = node2;
                node2 = node1.next;
            }
        }
        return newHead.next;

    }


}
