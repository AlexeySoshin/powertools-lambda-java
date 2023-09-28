package helloworld;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.xray.AWSXRay;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AppTest {

    @Before
    public void setup() {
        if (null == System.getenv("LAMBDA_TASK_ROOT")) {
            AWSXRay.beginSegment("test");
        }
    }

    @After
    public void tearDown() {
        if (AWSXRay.getCurrentSubsegmentOptional().isPresent()) {
            AWSXRay.endSubsegment();
        }

        if (null == System.getenv("LAMBDA_TASK_ROOT")) {
            AWSXRay.endSegment();
        }
    }

    @Test
    public void successfulResponse() {
        App app = new App();
        APIGatewayProxyResponseEvent result = app.handleRequest(null, null);
        assertEquals(200, result.getStatusCode().intValue());
        assertEquals("application/json", result.getHeaders().get("Content-Type"));
        String content = result.getBody();
        assertNotNull(content);
        assertTrue(content.contains("\"message\""));
        assertTrue(content.contains("\"hello world\""));
        assertTrue(content.contains("\"location\""));
    }
}
