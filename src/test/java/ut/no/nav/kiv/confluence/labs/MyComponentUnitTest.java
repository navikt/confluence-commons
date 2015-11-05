package ut.no.nav.kiv.confluence.labs;

import org.junit.Test;
import no.nav.kiv.confluence.labs.api.MyPluginComponent;
import no.nav.kiv.confluence.labs.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}