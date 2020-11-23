import nose.tools as nt 
from double_pendulum import DoublePendulum 
import numpy as np

def test_DoublePendulum():
	"""Checks that pendulums that are at rest stay at rest"""
	p = DoublePendulum(M1 = 2, L1 = 1.1, M2 = 0.5, L2 = 1.1)
	for i in p(0, (0, 0, 0, 0)):
		assert i == 0


def test_DoublePendulumCartesian():
	"""Checks if the cartesian coordinates are correct."""
	p = DoublePendulum(M1 = 2, L1 = 1.1, M2 = 0.5, L2 = 2.3)
	p.solve((np.pi/4, 0.1, np.pi/8, 0.2),10,0.1)
	for i in range(len(p.x1)):
		nt.assert_almost_equals(p.x1[i]**2 + p.y1[i]**2, 1.1**2)
	for i in range(len(p.x2)):
		nt.assert_almost_equals((p.x2[i] - p.x1[i])**2 + (p.y2[i] - p.y1[i])**2, 2.3**2)

@nt.raises(AttributeError)
def test_NotSolved():
	"""Checks that t, theta and theta2 raise and error 
	if the solve method hasn't been called.
	"""
	p = DoublePendulum(M1 = 2, L1 = 1.1,M2 = 0.5, L2 = 1.1)
	p.t
	p.theta
	p.theta2
