package com.linkedin.thirdeye.taskexecution.impl.dag;

import com.linkedin.thirdeye.taskexecution.dag.DAG;
import com.linkedin.thirdeye.taskexecution.dag.Node;
import com.linkedin.thirdeye.taskexecution.operator.Operator;
import com.linkedin.thirdeye.taskexecution.operator.OperatorConfig;
import com.linkedin.thirdeye.taskexecution.operator.OperatorContext;
import com.linkedin.thirdeye.taskexecution.operator.OperatorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;


public class LogicalPlanTest {
  private DAG dag;
  private Node root;

  @Test
  public void testCreation() {
    try {
      dag = new LogicalPlan();
    } catch (Exception e) {
      Assert.fail();
    }
  }

  @Test(dependsOnMethods = {"testCreation"})
  public void testAddRoot() {
    dag = new LogicalPlan();
    root = new LogicalNode("1", DummyOperator.class);
    dag.addNode(root);

    Assert.assertEquals(dag.getRootNodes().size(), 1);
    Assert.assertEquals(dag.getAllNodes().size(), 1);
    Assert.assertEquals(dag.size(), 1);
    Assert.assertEquals(dag.getLeafNodes().size(), 1);
  }

  @Test(dependsOnMethods = {"testCreation", "testAddRoot"})
  public void testAddDuplicatedNode() {
    Node node2 = new LogicalNode("1", DummyOperator.class);
    Node dagNode1 = dag.addNode(node2);

    Assert.assertEquals(dagNode1, root);
    Assert.assertEquals(dag.getRootNodes().size(), 1);
    Assert.assertEquals(dag.getAllNodes().size(), 1);
    Assert.assertEquals(dag.getLeafNodes().size(), 1);
  }

  @Test(dependsOnMethods = {"testCreation", "testAddRoot", "testAddDuplicatedNode"})
  public void testAddNodes() {
    Node node2 = new LogicalNode("2", DummyOperator.class);
    dag.addNode(node2);
    dag.addEdge(root, node2);
    Assert.assertEquals(dag.getRootNodes().size(), 1);
    Assert.assertEquals(dag.getAllNodes().size(), 2);
    Assert.assertEquals(dag.getLeafNodes().size(), 1);

    Node node3 = new LogicalNode("3", DummyOperator.class);
    // The following line should be automatically executed in the addEdge method.
    // dag.addNode(node3);
    dag.addEdge(root, node3);
    Assert.assertEquals(dag.getRootNodes().size(), 1);
    Assert.assertEquals(dag.getAllNodes().size(), 3);
    Assert.assertEquals(dag.getLeafNodes().size(), 2);
  }

  static class DummyOperator implements Operator {
    private static final Logger LOG = LoggerFactory.getLogger(DummyOperator.class);

    @Override
    public void initialize(OperatorConfig operatorConfig) {

    }

    @Override
    public OperatorResult run(OperatorContext operatorContext) {
      return null;
    }
  }
}